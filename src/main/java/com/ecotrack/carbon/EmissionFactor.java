package com.ecotrack.carbon;

import java.math.BigDecimal;
import java.util.Objects;

public record EmissionFactor(BigDecimal kilogramsOfCo2ePerKwh, String source) {

    public EmissionFactor {
        Objects.requireNonNull(kilogramsOfCo2ePerKwh, "kilogramsOfCo2ePerKwh must not be null");
        Objects.requireNonNull(source, "source must not be null");

        if (kilogramsOfCo2ePerKwh.signum() < 0) {
            throw new IllegalArgumentException("kilogramsOfCo2ePerKwh must not be negative");
        }
        if (source.isBlank()) {
            throw new IllegalArgumentException("source must not be blank");
        }
    }
}