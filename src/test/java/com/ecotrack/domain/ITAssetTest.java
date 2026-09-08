package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ITAssetTest {

    @Test
    void constructorAcceptsValidAsset() {
        assertDoesNotThrow(() -> new ITAsset(
                1L,
                7L,
                "ASSET-001",
                AssetType.LAPTOP,
                Optional.of("workstation-01"),
                Optional.of("Dell"),
                Optional.of("Latitude 7440"),
                Optional.of("SN-123"),
                Optional.of(LocalDate.of(2025, 9, 1)),
                AssetStatus.ACTIVE,
                Instant.parse("2026-01-01T00:00:00Z")));
    }

    @Test
    void constructorRejectsInvalidIdsAndRequiredFields() {
        assertThrows(IllegalArgumentException.class, () -> new ITAsset(
                0L, 7L, "ASSET-001", AssetType.LAPTOP, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new ITAsset(
                1L, 0L, "ASSET-001", AssetType.LAPTOP, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new ITAsset(
                1L, 7L, "   ", AssetType.LAPTOP, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE, Instant.now()));
        assertThrows(NullPointerException.class, () -> new ITAsset(
                1L, 7L, "ASSET-001", null, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE, Instant.now()));
        assertThrows(NullPointerException.class, () -> new ITAsset(
                1L, 7L, "ASSET-001", AssetType.LAPTOP, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), null, Instant.now()));
    }

    @Test
    void constructorRejectsBlankOptionalMetadataAndNullInstant() {
        assertThrows(IllegalArgumentException.class, () -> new ITAsset(
                1L, 7L, "ASSET-001", AssetType.LAPTOP, Optional.of("   "), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new ITAsset(
                1L, 7L, "ASSET-001", AssetType.LAPTOP, Optional.empty(), Optional.of("  "), Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE, Instant.now()));
        assertThrows(NullPointerException.class, () -> new ITAsset(
                1L, 7L, "ASSET-001", AssetType.LAPTOP, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE, null));
    }
}
