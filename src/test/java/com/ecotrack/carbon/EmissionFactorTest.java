package com.ecotrack.carbon;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EmissionFactorTest {

    @Test
    void constructorAcceptsValidValues() {
        assertDoesNotThrow(() -> new EmissionFactor(new BigDecimal("0.42"), "IEA"));
    }

    @Test
    void constructorRejectsNullValues() {
        assertThrows(NullPointerException.class, () -> new EmissionFactor(null, "IEA"));
        assertThrows(NullPointerException.class, () -> new EmissionFactor(new BigDecimal("0.42"), null));
    }

    @Test
    void constructorRejectsNegativeOrBlankValues() {
        assertThrows(IllegalArgumentException.class, () -> new EmissionFactor(new BigDecimal("-0.01"), "IEA"));
        assertThrows(IllegalArgumentException.class, () -> new EmissionFactor(new BigDecimal("0.42"), "   "));
    }
}
