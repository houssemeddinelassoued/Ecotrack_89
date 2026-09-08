package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DailyConsumptionTest {

    @Test
    void constructorAcceptsValidConsumption() {
        assertDoesNotThrow(() -> new DailyConsumption(
                1L,
                10L,
                LocalDate.of(2026, 1, 15),
                new BigDecimal("45.5"),
                Optional.of(new BigDecimal("19.11")),
                Optional.of("Smart meter"),
                Instant.parse("2026-01-15T08:00:00Z")));
    }

    @Test
    void constructorRejectsInvalidIdsAndDates() {
        assertThrows(IllegalArgumentException.class, () -> new DailyConsumption(
                0L, 10L, LocalDate.of(2026, 1, 15), new BigDecimal("10"), Optional.empty(), Optional.empty(), Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new DailyConsumption(
                1L, 0L, LocalDate.of(2026, 1, 15), new BigDecimal("10"), Optional.empty(), Optional.empty(), Instant.now()));
        assertThrows(NullPointerException.class, () -> new DailyConsumption(
                1L, 10L, null, new BigDecimal("10"), Optional.empty(), Optional.empty(), Instant.now()));
        assertThrows(NullPointerException.class, () -> new DailyConsumption(
                1L, 10L, LocalDate.of(2026, 1, 15), new BigDecimal("10"), Optional.empty(), Optional.empty(), null));
    }

    @Test
    void constructorRejectsNegativeValuesAndBlankOptionalText() {
        assertThrows(IllegalArgumentException.class, () -> new DailyConsumption(
                1L, 10L, LocalDate.of(2026, 1, 15), new BigDecimal("-1"), Optional.empty(), Optional.empty(), Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new DailyConsumption(
                1L, 10L, LocalDate.of(2026, 1, 15), new BigDecimal("10"), Optional.of(new BigDecimal("-0.5")), Optional.empty(), Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new DailyConsumption(
                1L, 10L, LocalDate.of(2026, 1, 15), new BigDecimal("10"), Optional.empty(), Optional.of("   "), Instant.now()));
    }
}
