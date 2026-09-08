package com.ecotrack.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public record DailyConsumption(
        long consumptionId,
        long assetId,
        LocalDate consumptionDate,
        BigDecimal energyInKwh,
        Optional<BigDecimal> carbonInKilogramsOfCo2e,
        Optional<String> dataSource,
        Instant recordedAt) {

    public DailyConsumption {
        requirePositive(consumptionId, "consumptionId");
        requirePositive(assetId, "assetId");
        consumptionDate = Objects.requireNonNull(consumptionDate, "consumptionDate must not be null");
        energyInKwh = requireNonNegative(energyInKwh, "energyInKwh");
        carbonInKilogramsOfCo2e = Objects.requireNonNull(
                carbonInKilogramsOfCo2e,
                "carbonInKilogramsOfCo2e must not be null");
        carbonInKilogramsOfCo2e.ifPresent(value -> requireNonNegative(value, "carbonInKilogramsOfCo2e"));
        dataSource = requireOptional(dataSource, "dataSource");
        recordedAt = Objects.requireNonNull(recordedAt, "recordedAt must not be null");
    }

    private static void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    private static BigDecimal requireNonNegative(BigDecimal value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.signum() < 0) {
            throw new IllegalArgumentException(name + " must not be negative");
        }
        return value;
    }

    private static Optional<String> requireOptional(Optional<String> value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        value.ifPresent(entry -> {
            if (entry.isBlank()) {
                throw new IllegalArgumentException(name + " must not be blank when present");
            }
        });
        return value;
    }
}