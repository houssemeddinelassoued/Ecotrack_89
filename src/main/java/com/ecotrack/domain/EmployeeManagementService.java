package com.ecotrack.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class EmployeeManagementService implements EmployeeLookup {

    private final Map<Long, Employee> employeesById = new HashMap<>();
    private final Map<EmployeeKey, Long> employeeIdsByNumber = new HashMap<>();
    private final Map<EmployeeKey, Long> employeeIdsByEmail = new HashMap<>();

    public Employee add(Employee employee) {
        Objects.requireNonNull(employee, "employee must not be null");
        var employeeNumberKey = new EmployeeKey(employee.companyId(), employee.employeeNumber());
        var emailKey = new EmployeeKey(employee.companyId(), employee.email());

        if (employeesById.containsKey(employee.employeeId())) {
            throw new DuplicateEmployeeException("employeeId already exists: " + employee.employeeId());
        }
        if (employeeIdsByNumber.containsKey(employeeNumberKey)) {
            throw new DuplicateEmployeeException("employeeNumber already exists for company: " + employee.companyId());
        }
        if (employeeIdsByEmail.containsKey(emailKey)) {
            throw new DuplicateEmployeeException("email already exists for company: " + employee.companyId());
        }

        employeesById.put(employee.employeeId(), employee);
        employeeIdsByNumber.put(employeeNumberKey, employee.employeeId());
        employeeIdsByEmail.put(emailKey, employee.employeeId());
        return employee;
    }

    @Override
    public Optional<Employee> findById(long employeeId) {
        return Optional.ofNullable(employeesById.get(employeeId));
    }

    public Optional<Employee> findByEmployeeNumber(long companyId, String employeeNumber) {
        return findByKey(employeeIdsByNumber, companyId, employeeNumber);
    }

    public Optional<Employee> findByEmail(long companyId, String email) {
        return findByKey(employeeIdsByEmail, companyId, email);
    }

    public Employee deactivate(long employeeId) {
        var employee = findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("employee does not exist: " + employeeId));
        var deactivatedEmployee = new Employee(
                employee.employeeId(),
                employee.companyId(),
                employee.employeeNumber(),
                employee.firstName(),
                employee.lastName(),
                employee.email(),
                employee.department(),
                false,
                employee.createdAt());
        employeesById.put(employeeId, deactivatedEmployee);
        return deactivatedEmployee;
    }

    private Optional<Employee> findByKey(Map<EmployeeKey, Long> employeeIds, long companyId, String value) {
        if (companyId <= 0) {
            throw new IllegalArgumentException("companyId must be positive");
        }
        Objects.requireNonNull(value, "value must not be null");
        return Optional.ofNullable(employeeIds.get(new EmployeeKey(companyId, value)))
                .flatMap(this::findById);
    }

    private record EmployeeKey(long companyId, String value) {
    }
}