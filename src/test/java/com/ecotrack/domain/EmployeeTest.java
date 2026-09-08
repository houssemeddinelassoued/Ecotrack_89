package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class EmployeeTest {

    @Test
    void constructorAcceptsValidEmployee() {
        assertDoesNotThrow(() -> new Employee(
                1L,
                5L,
                "EMP-001",
                "Alice",
                "Martin",
                "alice.martin@company.com",
                Optional.of("IT"),
                true,
                Instant.parse("2026-01-01T00:00:00Z")));
    }

    @Test
    void constructorRejectsInvalidIdsAndMandatoryFields() {
        assertThrows(IllegalArgumentException.class, () -> new Employee(
                0L, 5L, "EMP-001", "Alice", "Martin", "alice@company.com", Optional.empty(), true, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Employee(
                1L, 0L, "EMP-001", "Alice", "Martin", "alice@company.com", Optional.empty(), true, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Employee(
                1L, 5L, "   ", "Alice", "Martin", "alice@company.com", Optional.empty(), true, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Employee(
                1L, 5L, "EMP-001", "   ", "Martin", "alice@company.com", Optional.empty(), true, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Employee(
                1L, 5L, "EMP-001", "Alice", "Martin", "bad-email", Optional.empty(), true, Instant.now()));
    }

    @Test
    void constructorRejectsBlankOptionalDepartmentAndNullInstant() {
        assertThrows(IllegalArgumentException.class, () -> new Employee(
                1L, 5L, "EMP-001", "Alice", "Martin", "alice@company.com", Optional.of("   "), true, Instant.now()));
        assertThrows(NullPointerException.class, () -> new Employee(
                1L, 5L, "EMP-001", "Alice", "Martin", "alice@company.com", Optional.empty(), true, null));
    }
}
