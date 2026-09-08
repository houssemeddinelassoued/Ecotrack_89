package com.ecotrack.carbon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class EnergyBasedCarbonCalculator implements CarbonCalculator {

    private static final int CARBON_SCALE = 4;

    @Override
    public CarbonFootprint calculate(BigDecimal energyInKwh, EmissionFactor emissionFactor) {
        Objects.requireNonNull(energyInKwh, "energyInKwh must not be null");
        Objects.requireNonNull(emissionFactor, "emissionFactor must not be null");

        if (energyInKwh.signum() < 0) {
            throw new IllegalArgumentException("energyInKwh must not be negative");
        }

        var kilogramsOfCo2e = energyInKwh
                .multiply(emissionFactor.kilogramsOfCo2ePerKwh())
                .setScale(CARBON_SCALE, RoundingMode.HALF_UP);

        return new CarbonFootprint(kilogramsOfCo2e, emissionFactor);
    }
}