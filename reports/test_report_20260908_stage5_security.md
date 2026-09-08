# Stage 5 Security Review - EcoTrack

## 1. Status: WARN

Gate passes: no uncorrected Critical or High vulnerabilities were identified. Medium findings require remediation before exposing these services through concurrent or untrusted entry points.

## 2. Work performed

Reviewed the requested management services, lookup boundaries, supporting domain records, carbon calculation, Maven dependency graph, and targeted tests. Assessed input validation, in-memory state, lifecycle rules, concurrency, authorization readiness, and exception disclosure.

## 3. Files changed

`reports/test_report_20260908_stage5_security.md` only. No application code was modified.

## 4. Validation

- `mvn '-Dtest=EmployeeManagementServiceTest,AssetAssignmentManagementServiceTest,DailyConsumptionManagementServiceTest' test`: PASS; 11 tests, 0 failures/errors.
- `mvn test`: PASS; 37 tests, 0 failures/errors.
- `mvn dependency:tree -Dscope=test`: PASS; JUnit 5.13.4 and test-scoped transitive dependencies only. No runtime third-party dependencies found.
- Editor diagnostics: PASS; no errors in the three reviewed service files.
- Source-history diff was unavailable because this workspace is not a Git repository.

## 5. Risks

### Medium - Non-atomic mutable state allows invariant bypass under concurrency

- Evidence: `HashMap`/`ArrayList` state is mutated after separate duplicate/overlap checks in `src/main/java/com/ecotrack/domain/EmployeeManagementService.java` (lines 11-29), `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java` (lines 13, 21-43), and `src/main/java/com/ecotrack/domain/DailyConsumptionManagementService.java` (lines 12-13, 25-49). None of the public operations synchronize or otherwise make check-and-insert atomic.
- Impact: concurrent callers can each pass uniqueness or overlap checks before either writes, creating duplicate employees, overlapping assignments, or duplicate asset/date consumption records. Concurrent iteration and mutation can also cause inconsistent results or runtime failures.
- File: `src/main/java/com/ecotrack/domain/EmployeeManagementService.java`; `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java`; `src/main/java/com/ecotrack/domain/DailyConsumptionManagementService.java`.
- Mitigation: keep these objects single-thread confined until persistence is added, or guard each complete read-validate-write transaction with a shared lock. At the persistence boundary, enforce unique indexes and transactional exclusion/locking for assignment periods; add concurrent-operation tests.

### Medium - Inactive employees can receive asset assignments

- Evidence: `AssetAssignmentManagementService.add` verifies asset status and company equality, but does not check `employee.active()` before storing the assignment in `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java` (lines 29-42). `EmployeeManagementService.deactivate` persists an employee with `active` set to `false` in `src/main/java/com/ecotrack/domain/EmployeeManagementService.java` (lines 45-58).
- Impact: an offboarded or suspended employee may be assigned an active asset, undermining asset custody and lifecycle controls once this domain is invoked by a UI, job, or API.
- File: `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java`; `src/main/java/com/ecotrack/domain/EmployeeManagementService.java`.
- Mitigation: reject assignments when `!employee.active()` and add a test covering a deactivated employee. Decide and document whether deactivation must also close an existing active assignment.

### Medium - Assignment closure accepts an uncontrolled future date

- Evidence: `closeActiveAssignment` checks only that `assignedTo` is non-null and then constructs a closed record in `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java` (lines 45-64). `AssetAssignment` only rejects an end date before its start date in `src/main/java/com/ecotrack/domain/AssetAssignment.java` (lines 22-24).
- Impact: a caller can close a current assignment with an arbitrary future date, falsely extending custody and blocking a legitimate next assignment. This weakens the acceptance constraint for controlled closure and creates an audit-data integrity risk.
- File: `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java`; `src/main/java/com/ecotrack/domain/AssetAssignment.java`.
- Mitigation: define the allowed closure policy and enforce it, typically `assignedTo <= LocalDate.now(clock)` using an injected `Clock`; retain the existing `assignedTo >= assignedFrom` validation and add boundary tests.

### Low - Employee email uniqueness is bypassable through case variants

- Evidence: `EmployeeManagementService` uses the raw email string as an `EmployeeKey` in `src/main/java/com/ecotrack/domain/EmployeeManagementService.java` (lines 16-22, 69-70). `Employee` requires only a nonblank address containing `@` in `src/main/java/com/ecotrack/domain/Employee.java` (lines 41-47). Therefore `Alex@example.com` and `alex@example.com` are distinct keys for the same company.
- Impact: likely duplicate identities can be created, weakening the required company/email uniqueness rule and complicating future authentication or account reconciliation.
- File: `src/main/java/com/ecotrack/domain/EmployeeManagementService.java`; `src/main/java/com/ecotrack/domain/Employee.java`.
- Mitigation: define canonical email semantics, usually trim and lowercase using `Locale.ROOT` before keying and lookup, and preserve the original display value separately if required. Add variant-case and whitespace tests.

### Low - Domain exceptions reveal record existence and identifiers

- Evidence: exception messages disclose supplied IDs and distinguish missing asset, missing employee, duplicate ID, and active-assignment states in `src/main/java/com/ecotrack/domain/EmployeeManagementService.java` (lines 19, 48), `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java` (lines 23-30, 63), and `src/main/java/com/ecotrack/domain/DailyConsumptionManagementService.java` (lines 27-34).
- Impact: if propagated verbatim by a future API, an unauthenticated or cross-tenant caller could enumerate employees, assets, assignments, and consumption existence.
- File: `src/main/java/com/ecotrack/domain/EmployeeManagementService.java`; `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java`; `src/main/java/com/ecotrack/domain/DailyConsumptionManagementService.java`.
- Mitigation: at the delivery boundary, authenticate and authorize before service invocation, map domain errors to non-enumerating client responses, and retain detailed IDs only in access-controlled logs.

### Info - Authorization and tenant access control must be introduced at the future trust boundary

- Evidence: the lookup interfaces accept only resource IDs in `src/main/java/com/ecotrack/domain/AssetLookup.java` and `src/main/java/com/ecotrack/domain/EmployeeLookup.java`; no caller identity, company context, policy, API, persistence, or security framework exists in this pure-domain project.
- Impact: this is not an exploitable vulnerability in the current in-process test application. It becomes an insecure direct object reference risk if future adapters accept user-controlled IDs without verifying tenant membership and operation permissions.
- File: `src/main/java/com/ecotrack/domain/AssetLookup.java`; `src/main/java/com/ecotrack/domain/EmployeeLookup.java`; `src/main/java/com/ecotrack/domain/AssetAssignmentManagementService.java`; `src/main/java/com/ecotrack/domain/DailyConsumptionManagementService.java`.
- Mitigation: introduce authenticated principal and tenant context at the application/API layer; authorize every read and mutation against company membership and role, and make repository queries tenant-scoped. Keep domain records free of transport/security concerns.

## 6. Next action

Remediate the three Medium findings, add focused negative and concurrent tests, then rerun this Stage 5 audit before adding API or persistence adapters. The current gate is PASS for Critical/High only, with WARN status until Medium risks are resolved.