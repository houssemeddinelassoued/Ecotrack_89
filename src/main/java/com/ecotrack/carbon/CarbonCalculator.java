package com.ecotrack.carbon;

import java.math.BigDecimal;

public interface CarbonCalculator {

    CarbonFootprint calculate(BigDecimal energyInKwh, EmissionFactor emissionFactor);
}