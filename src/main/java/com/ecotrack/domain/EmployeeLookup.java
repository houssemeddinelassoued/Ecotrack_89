package com.ecotrack.domain;

import java.util.Optional;

@FunctionalInterface
public interface EmployeeLookup {

    Optional<Employee> findById(long employeeId);
}