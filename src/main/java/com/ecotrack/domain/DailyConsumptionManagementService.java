package com.ecotrack.domain;

import com.ecotrack.carbon.CarbonCalculator;
import com.ecotrack.carbon.EmissionFactor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DailyConsumptionManagementService {

    private final AssetLookup assetLookup;
    private final CarbonCalculator carbonCalculator;
    private final EmissionFactor emissionFactor;
    private final Map<ConsumptionKey, DailyConsumption> consumptions = new HashMap<>();
    private final Map<Long, DailyConsumption> consumptionsById = new HashMap<>();

    public DailyConsumptionManagementService(
            AssetLookup assetLookup,
            CarbonCalculator carbonCalculator,
            EmissionFactor emissionFactor) {
        this.assetLookup = Objects.requireNonNull(assetLookup, "assetLookup must not be null");
        this.carbonCalculator = Objects.requireNonNull(carbonCalculator, "carbonCalculator must not be null");
        this.emissionFactor = Objects.requireNonNull(emissionFactor, "emissionFactor must not be null");
    }

    public DailyConsumption add(DailyConsumption consumption) {
        Objects.requireNonNull(consumption, "consumption must not be null");
        if (consumptionsById.containsKey(consumption.consumptionId())) {
            throw new IllegalStateException("consumptionId already exists: " + consumption.consumptionId());
        }
        assetLookup.findById(consumption.assetId())
                .orElseThrow(() -> new IllegalArgumentException("asset does not exist: " + consumption.assetId()));
        var key = new ConsumptionKey(consumption.assetId(), consumption.consumptionDate());
        if (consumptions.containsKey(key)) {
            throw new IllegalStateException("consumption already exists for asset and date");
        }

        var carbon = carbonCalculator.calculate(consumption.energyInKwh(), emissionFactor).kilogramsOfCo2e();
        var calculatedConsumption = new DailyConsumption(
                consumption.consumptionId(),
                consumption.assetId(),
                consumption.consumptionDate(),
                consumption.energyInKwh(),
                java.util.Optional.of(carbon),
                consumption.dataSource(),
                consumption.recordedAt());
        consumptions.put(key, calculatedConsumption);
        consumptionsById.put(calculatedConsumption.consumptionId(), calculatedConsumption);
        return calculatedConsumption;
    }

    public BigDecimal aggregateEnergyForDate(LocalDate consumptionDate) {
        Objects.requireNonNull(consumptionDate, "consumptionDate must not be null");
        return consumptions.values().stream()
                .filter(consumption -> consumption.consumptionDate().equals(consumptionDate))
                .map(DailyConsumption::energyInKwh)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private record ConsumptionKey(long assetId, LocalDate consumptionDate) {
    }
}