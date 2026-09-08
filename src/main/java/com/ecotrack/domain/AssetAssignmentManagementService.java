package com.ecotrack.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class AssetAssignmentManagementService {

    private final AssetLookup assetLookup;
    private final EmployeeLookup employeeLookup;
    private final List<AssetAssignment> assignments = new ArrayList<>();
    private final Map<Long, AssetAssignment> assignmentsById = new HashMap<>();

    public AssetAssignmentManagementService(AssetLookup assetLookup, EmployeeLookup employeeLookup) {
        this.assetLookup = Objects.requireNonNull(assetLookup, "assetLookup must not be null");
        this.employeeLookup = Objects.requireNonNull(employeeLookup, "employeeLookup must not be null");
    }

    public AssetAssignment add(AssetAssignment assignment) {
        Objects.requireNonNull(assignment, "assignment must not be null");
        if (assignmentsById.containsKey(assignment.assignmentId())) {
            throw new IllegalStateException("assignmentId already exists: " + assignment.assignmentId());
        }
        var asset = assetLookup.findById(assignment.assetId())
                .orElseThrow(() -> new IllegalArgumentException("asset does not exist: " + assignment.assetId()));
        var employee = employeeLookup.findById(assignment.employeeId())
                .orElseThrow(() -> new IllegalArgumentException("employee does not exist: " + assignment.employeeId()));

        if (asset.status() != AssetStatus.ACTIVE) {
            throw new IllegalStateException("asset must be active to be assigned: " + asset.assetId());
        }
        if (asset.companyId() != employee.companyId()) {
            throw new IllegalArgumentException("asset and employee must belong to the same company");
        }
        if (assignments.stream().anyMatch(existing -> periodsOverlap(existing, assignment))) {
            throw new IllegalStateException("asset assignment period overlaps an existing assignment: " + asset.assetId());
        }

        assignments.add(assignment);
        assignmentsById.put(assignment.assignmentId(), assignment);
        return assignment;
    }

    public AssetAssignment closeActiveAssignment(long assetId, LocalDate assignedTo) {
        Objects.requireNonNull(assignedTo, "assignedTo must not be null");
        for (var index = 0; index < assignments.size(); index++) {
            var assignment = assignments.get(index);
            if (assignment.assetId() == assetId && assignment.isActive()) {
                var closedAssignment = new AssetAssignment(
                        assignment.assignmentId(),
                        assignment.assetId(),
                        assignment.employeeId(),
                        assignment.assignedFrom(),
                        java.util.Optional.of(assignedTo),
                        assignment.assignmentNotes());
                assignments.set(index, closedAssignment);
                assignmentsById.put(closedAssignment.assignmentId(), closedAssignment);
                return closedAssignment;
            }
        }
        throw new IllegalArgumentException("active assignment does not exist for asset: " + assetId);
    }

    private static boolean periodsOverlap(AssetAssignment first, AssetAssignment second) {
        if (first.assetId() != second.assetId()) {
            return false;
        }
        var firstEndsBeforeSecondStarts = first.assignedTo()
                .map(end -> end.isBefore(second.assignedFrom()))
                .orElse(false);
        var secondEndsBeforeFirstStarts = second.assignedTo()
                .map(end -> end.isBefore(first.assignedFrom()))
                .orElse(false);
        return !firstEndsBeforeSecondStarts && !secondEndsBeforeFirstStarts;
    }
}