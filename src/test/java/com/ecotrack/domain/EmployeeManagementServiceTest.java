package com.ecotrack.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class EmployeeManagementServiceTest {

    @Test
    void addsAndFindsEmployeeByIdNumberAndEmailWithinCompany() {
        var service = new EmployeeManagementService();
        var employee = employee(1, 10, "E-001", "alex@example.com");

        assertSame(employee, service.add(employee));
        assertEquals(Optional.of(employee), service.findById(1));
        assertEquals(Optional.of(employee), service.findByEmployeeNumber(10, "E-001"));
        assertEquals(Optional.of(employee), service.findByEmail(10, "alex@example.com"));
        assertTrue(service.findByEmployeeNumber(11, "E-001").isEmpty());
        assertTrue(service.findByEmail(11, "alex@example.com").isEmpty());
    }

    @Test
    void deactivatesEmployeeAndPersistsTheDeactivatedRecord() {
        var service = new EmployeeManagementService();
        service.add(employee(1, 10, "E-001", "alex@example.com"));

        var deactivated = service.deactivate(1);

        assertFalse(deactivated.active());
        assertEquals(Optional.of(deactivated), service.findById(1));
    }

    @Test
    void rejectsDuplicateEmployeeNumberWithinTheSameCompany() {
        var service = new EmployeeManagementService();
        service.add(employee(1, 10, "E-001", "alex@example.com"));

        assertThrows(DuplicateEmployeeException.class,
                () -> service.add(employee(2, 10, "E-001", "bea@example.com")));
    }

    @Test
    void rejectsDuplicateEmailWithinTheSameCompanyButAllowsItAcrossCompanies() {
        var service = new EmployeeManagementService();
        service.add(employee(1, 10, "E-001", "alex@example.com"));
        var sameEmailInAnotherCompany = employee(3, 11, "E-001", "alex@example.com");

        assertThrows(DuplicateEmployeeException.class,
                () -> service.add(employee(2, 10, "E-002", "alex@example.com")));
        assertSame(sameEmailInAnotherCompany, service.add(sameEmailInAnotherCompany));
    }

    private static Employee employee(long id, long companyId, String number, String email) {
        return new Employee(id, companyId, number, "Alex", "Smith", email, Optional.empty(), true,
                Instant.parse("2026-09-01T00:00:00Z"));
    }
}