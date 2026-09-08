package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ecotrack.carbon.EnergyBasedCarbonCalculator;
import com.ecotrack.carbon.EmissionFactor;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DailyConsumptionManagementServiceTest {

    private static final EmissionFactor FACTOR = new EmissionFactor(new BigDecimal("0.0560"), "ADEME");

    @Test
    void addsConsumptionAndCalculatesCarbonToFourDecimalPlaces() {
        var service = serviceWithAsset(1);

        var added = service.add(consumption(1, 1, "2026-09-01", "12.3456"));

        assertEquals(Optional.of(new BigDecimal("0.6914")), added.carbonInKilogramsOfCo2e());
    }

    @Test
    void rejectsConsumptionForMissingAssetAndDuplicateAssetDate() {
        var service = serviceWithAsset(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.add(consumption(1, 2, "2026-09-01", "1.0000")));
        service.add(consumption(1, 1, "2026-09-01", "1.0000"));
        assertThrows(IllegalStateException.class,
                () -> service.add(consumption(2, 1, "2026-09-01", "2.0000")));
    }

    @Test
    void aggregatesEnergyByConsumptionDateAcrossAssets() {
        var service = serviceWithAssets(1, 2);
        service.add(consumption(1, 1, "2026-09-01", "1.2500"));
        service.add(consumption(2, 2, "2026-09-01", "2.7500"));
        service.add(consumption(3, 1, "2026-09-02", "9.0000"));

        assertEquals(new BigDecimal("4.0000"), service.aggregateEnergyForDate(LocalDate.parse("2026-09-01")));
        assertEquals(BigDecimal.ZERO, service.aggregateEnergyForDate(LocalDate.parse("2026-09-03")));
    }

    private static DailyConsumptionManagementService serviceWithAsset(long assetId) {
        return serviceWithAssets(assetId);
    }

    private static DailyConsumptionManagementService serviceWithAssets(long... assetIds) {
        return new DailyConsumptionManagementService(
                assetId -> java.util.Arrays.stream(assetIds).anyMatch(id -> id == assetId)
                        ? Optional.of(asset(assetId)) : Optional.empty(),
                new EnergyBasedCarbonCalculator(), FACTOR);
    }

    private static ITAsset asset(long id) {
        return new ITAsset(id, 10, "AST-" + id, AssetType.LAPTOP, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), AssetStatus.ACTIVE,
                Instant.parse("2026-09-01T00:00:00Z"));
    }

    private static DailyConsumption consumption(long id, long assetId, String date, String energy) {
        return new DailyConsumption(id, assetId, LocalDate.parse(date), new BigDecimal(energy), Optional.empty(),
                Optional.of("meter"), Instant.parse("2026-09-01T00:00:00Z"));
    }
}