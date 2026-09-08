package com.ecotrack.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public record ITAsset(
        long assetId,
        long companyId,
        String assetTag,
        AssetType assetType,
        Optional<String> hostName,
        Optional<String> manufacturer,
        Optional<String> model,
        Optional<String> serialNumber,
        Optional<LocalDate> purchaseDate,
        AssetStatus status,
        Instant createdAt) {

    public ITAsset {
        requirePositive(assetId, "assetId");
        requirePositive(companyId, "companyId");
        assetTag = requireNonBlank(assetTag, "assetTag");
        assetType = Objects.requireNonNull(assetType, "assetType must not be null");
        hostName = requireOptional(hostName, "hostName");
        manufacturer = requireOptional(manufacturer, "manufacturer");
        model = requireOptional(model, "model");
        serialNumber = requireOptional(serialNumber, "serialNumber");
        purchaseDate = Objects.requireNonNull(purchaseDate, "purchaseDate must not be null");
        status = Objects.requireNonNull(status, "status must not be null");
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
}