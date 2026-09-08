package com.ecotrack.carbon;

import java.math.BigDecimal;
import java.util.Objects;

public record CarbonFootprint(BigDecimal kilogramsOfCo2e, EmissionFactor emissionFactor) {

    public CarbonFootprint {
        Objects.requireNonNull(kilogramsOfCo2e, "kilogramsOfCo2e must not be null");
        Objects.requireNonNull(emissionFactor, "emissionFactor must not be null");

        if (kilogramsOfCo2e.signum() < 0) {
            throw new IllegalArgumentException("kilogramsOfCo2e must not be negative");
        }
    }
}