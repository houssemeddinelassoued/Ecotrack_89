package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void constructorAcceptsValidCompany() {
        assertDoesNotThrow(() -> new Company(
                1L,
                "EcoTrack SAS",
                Optional.of("FR123456789"),
                "FR",
                true,
                Instant.parse("2026-01-01T00:00:00Z")));
    }

    @Test
    void constructorRejectsInvalidCompanyData() {
        assertThrows(IllegalArgumentException.class, () -> new Company(
                0L, "EcoTrack SAS", Optional.empty(), "FR", true, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Company(
                1L, "   ", Optional.empty(), "FR", true, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Company(
                1L, "EcoTrack SAS", Optional.of("  "), "FR", true, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new Company(
                1L, "EcoTrack SAS", Optional.empty(), "fra", true, Instant.now()));
    }
}
