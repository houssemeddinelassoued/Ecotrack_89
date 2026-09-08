package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AssetAssignmentManagementServiceTest {

    @Test
    void addsAssignmentForActiveAssetAndEmployeeInSameCompany() {
        var asset = asset(1, 10, AssetStatus.ACTIVE);
        var employee = employee(2, 10);
        var service = service(asset, employee);
        var assignment = assignment(1, 1, 2, "2026-09-01", Optional.empty());

        assertEquals(assignment, service.add(assignment));
    }

    @Test
    void rejectsAssignmentForMissingEmployeeInactiveAssetOrDifferentCompany() {
        var activeAsset = asset(1, 10, AssetStatus.ACTIVE);
        var employee = employee(2, 10);

        assertThrows(IllegalArgumentException.class,
            () -> service(activeAsset, employee).add(assignment(1, 3, 2, "2026-09-01", Optional.empty())));
        assertThrows(IllegalArgumentException.class,
                () -> service(activeAsset).add(assignment(1, 1, 2, "2026-09-01", Optional.empty())));
        assertThrows(IllegalStateException.class,
                () -> service(asset(1, 10, AssetStatus.RETIRED), employee)
                        .add(assignment(1, 1, 2, "2026-09-01", Optional.empty())));
        assertThrows(IllegalArgumentException.class,
                () -> service(activeAsset, employee(2, 11))
                        .add(assignment(1, 1, 2, "2026-09-01", Optional.empty())));
    }

    @Test
    void rejectsOverlappingPeriodsForSameAssetButAllowsAdjacentPeriods() {
        var asset = asset(1, 10, AssetStatus.ACTIVE);
        var employee = employee(2, 10);
        var service = service(asset, employee);
        service.add(assignment(1, 1, 2, "2026-09-01", Optional.of(LocalDate.parse("2026-09-10"))));

        assertThrows(IllegalStateException.class,
                () -> service.add(assignment(2, 1, 2, "2026-09-10", Optional.of(LocalDate.parse("2026-09-12")))));
        assertEquals(assignment(3, 1, 2, "2026-09-11", Optional.empty()),
                service.add(assignment(3, 1, 2, "2026-09-11", Optional.empty())));
    }

    @Test
    void closesTheActiveAssignmentForAnAsset() {
        var service = service(asset(1, 10, AssetStatus.ACTIVE), employee(2, 10));
        service.add(assignment(1, 1, 2, "2026-09-01", Optional.empty()));

        var closed = service.closeActiveAssignment(1, LocalDate.parse("2026-09-15"));

        assertEquals(Optional.of(LocalDate.parse("2026-09-15")), closed.assignedTo());
        assertTrue(!closed.isActive());
    }

    private static AssetAssignmentManagementService service(ITAsset asset, Employee... employees) {
        var employeeById = java.util.Arrays.stream(employees)
            .collect(java.util.stream.Collectors.toMap(Employee::employeeId, employee -> employee));
        return new AssetAssignmentManagementService(
                assetId -> assetId == asset.assetId() ? Optional.of(asset) : Optional.empty(),
                employeeId -> Optional.ofNullable(employeeById.get(employeeId)));
    }

    private static ITAsset asset(long id, long companyId, AssetStatus status) {
        return new ITAsset(id, companyId, "AST-" + id, AssetType.LAPTOP, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), status, Instant.parse("2026-09-01T00:00:00Z"));
    }

    private static Employee employee(long id, long companyId) {
        return new Employee(id, companyId, "E-" + id, "Alex", "Smith", "alex" + id + "@example.com",
                Optional.empty(), true, Instant.parse("2026-09-01T00:00:00Z"));
    }

    private static AssetAssignment assignment(long id, long assetId, long employeeId, String from, Optional<LocalDate> to) {
        return new AssetAssignment(id, assetId, employeeId, LocalDate.parse(from), to, Optional.empty());
    }
}