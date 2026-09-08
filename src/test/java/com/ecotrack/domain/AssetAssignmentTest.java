package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class AssetAssignmentTest {

    @Test
    void constructorAcceptsValidAssignment() {
        var assignedFrom = LocalDate.of(2026, 1, 5);

        assertDoesNotThrow(() -> new AssetAssignment(
                1L, 10L, 20L, assignedFrom, Optional.empty(), Optional.of("Laptop assigned")));
    }

    @Test
    void isActiveReturnsTrueWhenNoEndingDateIsPresent() {
        var assignment = new AssetAssignment(
                1L, 10L, 20L, LocalDate.of(2026, 1, 5), Optional.empty(), Optional.empty());

        assertTrue(assignment.isActive());
    }

    @Test
    void isActiveReturnsFalseWhenEndingDateIsPresent() {
        var assignment = new AssetAssignment(
                1L, 10L, 20L,
                LocalDate.of(2026, 1, 5),
                Optional.of(LocalDate.of(2026, 2, 1)),
                Optional.empty());

        assertFalse(assignment.isActive());
    }

    @Test
    void constructorRejectsNonPositiveIds() {
        assertThrows(IllegalArgumentException.class, () -> new AssetAssignment(
                0L, 10L, 20L, LocalDate.of(2026, 1, 5), Optional.empty(), Optional.empty()));
        assertThrows(IllegalArgumentException.class, () -> new AssetAssignment(
                1L, 0L, 20L, LocalDate.of(2026, 1, 5), Optional.empty(), Optional.empty()));
        assertThrows(IllegalArgumentException.class, () -> new AssetAssignment(
                1L, 10L, 0L, LocalDate.of(2026, 1, 5), Optional.empty(), Optional.empty()));
    }

    @Test
    void constructorRejectsInvalidDateAndBlankNotes() {
        assertThrows(IllegalArgumentException.class, () -> new AssetAssignment(
                1L, 10L, 20L,
                LocalDate.of(2026, 2, 1),
                Optional.of(LocalDate.of(2026, 1, 5)),
                Optional.empty()));
        assertThrows(IllegalArgumentException.class, () -> new AssetAssignment(
                1L, 10L, 20L,
                LocalDate.of(2026, 1, 5),
                Optional.empty(),
                Optional.of("   ")));
    }
}
