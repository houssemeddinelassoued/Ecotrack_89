package com.ecotrack.carbon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CarbonFootprintTest {

    @Test
    void constructorAcceptsValidValues() {
        var factor = new EmissionFactor(new BigDecimal("0.42"), "IEA");

        assertDoesNotThrow(() -> new CarbonFootprint(BigDecimal.ZERO, factor));
    }

    @Test
    void constructorRejectsNullValues() {
        var factor = new EmissionFactor(new BigDecimal("0.42"), "IEA");

        assertThrows(NullPointerException.class, () -> new CarbonFootprint(null, factor));
        assertThrows(NullPointerException.class, () -> new CarbonFootprint(BigDecimal.ZERO, null));
    }

    @Test
    void constructorRejectsNegativeValues() {
        var factor = new EmissionFactor(new BigDecimal("0.42"), "IEA");

        assertThrows(IllegalArgumentException.class, () -> new CarbonFootprint(new BigDecimal("-0.01"), factor));
    }
}
