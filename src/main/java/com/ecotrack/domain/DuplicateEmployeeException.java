package com.ecotrack.domain;

public final class DuplicateEmployeeException extends IllegalStateException {

    public DuplicateEmployeeException(String message) {
        super(message);
    }
}