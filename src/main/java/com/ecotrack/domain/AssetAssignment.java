package com.ecotrack.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public record AssetAssignment(
        long assignmentId,
        long assetId,
        long employeeId,
        LocalDate assignedFrom,
        Optional<LocalDate> assignedTo,
        Optional<String> assignmentNotes) {

    public AssetAssignment {
        requirePositive(assignmentId, "assignmentId");
        requirePositive(assetId, "assetId");
        requirePositive(employeeId, "employeeId");
        assignedFrom = Objects.requireNonNull(assignedFrom, "assignedFrom must not be null");
        assignedTo = Objects.requireNonNull(assignedTo, "assignedTo must not be null");
        assignmentNotes = Objects.requireNonNull(assignmentNotes, "assignmentNotes must not be null");

        if (assignedTo.isPresent() && assignedTo.get().isBefore(assignedFrom)) {
            throw new IllegalArgumentException("assignedTo must not precede assignedFrom");
        }
        assignmentNotes.ifPresent(note -> {
            if (note.isBlank()) {
                throw new IllegalArgumentException("assignmentNotes must not be blank when present");
            }
        });
    }

    public boolean isActive() {
        return assignedTo.isEmpty();
    }

    private static void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }
}