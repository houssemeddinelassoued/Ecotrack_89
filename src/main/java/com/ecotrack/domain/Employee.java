package com.ecotrack.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public record Employee(
        long employeeId,
        long companyId,
        String employeeNumber,
        String firstName,
        String lastName,
        String email,
        Optional<String> department,
        boolean active,
        Instant createdAt) {

    public Employee {
        requirePositive(employeeId, "employeeId");
        requirePositive(companyId, "companyId");
        employeeNumber = requireNonBlank(employeeNumber, "employeeNumber");
        firstName = requireNonBlank(firstName, "firstName");
        lastName = requireNonBlank(lastName, "lastName");
        email = requireEmail(email);
        department = requireOptional(department, "department");
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

    private static String requireEmail(String value) {
        value = requireNonBlank(value, "email");
        if (!value.contains("@")) {
            throw new IllegalArgumentException("email must contain @");
        }
        return value;
    }

    private static Optional<String> requireOptional(Optional<String> value, String name) {
        Objects.requireNonNull(value, name + " must not be null");
        value.ifPresent(entry -> requireNonBlank(entry, name));
        return value;
    }
}