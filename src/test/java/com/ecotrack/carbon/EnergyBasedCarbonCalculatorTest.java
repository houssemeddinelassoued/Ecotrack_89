package com.ecotrack.carbon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class EnergyBasedCarbonCalculatorTest {

    private final CarbonCalculator calculator = new EnergyBasedCarbonCalculator();
    private final EmissionFactor franceElectricityFactor = new EmissionFactor(
            new BigDecimal("0.0560"),
            "ADEME electricity mix");

    @Test
    void calculatesCarbonFootprintUsingTheProvidedEmissionFactor() {
        var footprint = calculator.calculate(new BigDecimal("12.3456"), franceElectricityFactor);

        assertEquals(new BigDecimal("0.6914"), footprint.kilogramsOfCo2e());
        assertEquals(franceElectricityFactor, footprint.emissionFactor());
    }

    @Test
    void calculatesZeroFootprintForZeroEnergyConsumption() {
        var footprint = calculator.calculate(BigDecimal.ZERO, franceElectricityFactor);

        assertEquals(new BigDecimal("0.0000"), footprint.kilogramsOfCo2e());
    }

    @Test
    void rejectsNegativeEnergyConsumption() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(new BigDecimal("-0.0001"), franceElectricityFactor));
    }

    @Test
    void rejectsNegativeEmissionFactors() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new EmissionFactor(new BigDecimal("-0.0560"), "Invalid factor"));
    }
}