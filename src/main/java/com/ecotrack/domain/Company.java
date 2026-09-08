package com.ecotrack.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public record Company(
        long companyId,
        String legalName,
        Optional<String> registrationNumber,
        String countryCode,
        boolean active,
        Instant createdAt) {

    public Company {
        requirePositive(companyId, "companyId");
        legalName = requireNonBlank(legalName, "legalName");
        registrationNumber = requireOptional(registrationNumber, "registrationNumber");
        countryCode = requireCountryCode(countryCode);
        createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    private static void requirePositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
    }

    private static String requireNonBlank(String value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(name + " must not be blank");
        }
        return value;
    }

    private static Optional<String> requireOptional(Optional<String> value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        value.ifPresent(entry -> requireNonBlank(entry, name));
        return value;
    }

    private static String requireCountryCode(String value) {
        value = requireNonBlank(value, "countryCode");
        if (!value.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("countryCode must contain two uppercase letters");
        }
        return value;
    }
}