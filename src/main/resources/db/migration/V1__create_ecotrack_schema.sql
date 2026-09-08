-- Flyway migration V1: EcoTrack PostgreSQL schema converted from EcoTrack-DDL.sql (SQL Server).
-- Mapping: IDENTITY -> GENERATED ALWAYS AS IDENTITY, nvarchar -> varchar/text,
-- datetime2 -> timestamptz, bit -> boolean, SYSUTCDATETIME() -> now() (stored in UTC by timestamptz).
-- Table and column names use snake_case; see this header as the SQL Server name mapping reference.

CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE company
(
    company_id          integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    legal_name          varchar(200) NOT NULL,
    registration_number varchar(50),
    country_code        char(2) NOT NULL,
    is_active           boolean NOT NULL DEFAULT true,
    created_at          timestamptz NOT NULL DEFAULT now(),

    CONSTRAINT uq_company_registration_number UNIQUE (registration_number),
    CONSTRAINT ck_company_country_code CHECK (country_code ~ '^[A-Z]{2}$')
);

CREATE TABLE employee
(
    employee_id     integer GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    company_id      integer NOT NULL REFERENCES company (company_id),
    employee_number varchar(50) NOT NULL,
    first_name      varchar(100) NOT NULL,
    last_name       varchar(100) NOT NULL,
    email           varchar(320) NOT NULL,
    department      varchar(150),
    is_active       boolean NOT NULL DEFAULT true,
    created_at      timestamptz NOT NULL DEFAULT now(),

    CONSTRAINT uq_employee_company_number UNIQUE (company_id, employee_number),
    CONSTRAINT uq_employee_company_email UNIQUE (company_id, email)
);

CREATE TABLE it_asset
(
    asset_id      bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    company_id    integer NOT NULL REFERENCES company (company_id),
    asset_tag     varchar(100) NOT NULL,
    asset_type    varchar(20) NOT NULL,
    host_name     varchar(255),
    manufacturer  varchar(100),
    model         varchar(150),
    serial_number varchar(150),
    purchase_date date,
    status        varchar(20) NOT NULL DEFAULT 'ACTIVE',
    created_at    timestamptz NOT NULL DEFAULT now(),

    CONSTRAINT uq_it_asset_company_tag UNIQUE (company_id, asset_tag),
    CONSTRAINT ck_it_asset_type CHECK (asset_type IN ('LAPTOP', 'SERVER')),
    CONSTRAINT ck_it_asset_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'RETIRED', 'DISPOSED'))
);

CREATE UNIQUE INDEX ux_it_asset_company_serial_number
    ON it_asset (company_id, serial_number)
    WHERE serial_number IS NOT NULL;

CREATE TABLE asset_assignment
(
    assignment_id    bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    asset_id         bigint NOT NULL REFERENCES it_asset (asset_id),
    employee_id      integer NOT NULL REFERENCES employee (employee_id),
    assigned_from    date NOT NULL,
    assigned_to      date,
    assignment_notes varchar(500),

    CONSTRAINT ck_asset_assignment_date_range CHECK (assigned_to IS NULL OR assigned_to >= assigned_from),
    CONSTRAINT ex_asset_assignment_no_overlap EXCLUDE USING gist (
        asset_id WITH =,
        daterange(assigned_from, COALESCE(assigned_to, 'infinity'::date), '[]') WITH &&
    )
);

CREATE TABLE daily_consumption
(
    consumption_id   bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    asset_id         bigint NOT NULL REFERENCES it_asset (asset_id),
    consumption_date date NOT NULL,
    energy_kwh       numeric(12, 4) NOT NULL,
    carbon_kg_co2e   numeric(12, 4),
    data_source      varchar(100),
    recorded_at      timestamptz NOT NULL DEFAULT now(),

    CONSTRAINT uq_daily_consumption_asset_date UNIQUE (asset_id, consumption_date),
    CONSTRAINT ck_daily_consumption_energy_kwh CHECK (energy_kwh >= 0),
    CONSTRAINT ck_daily_consumption_carbon_kg_co2e CHECK (carbon_kg_co2e IS NULL OR carbon_kg_co2e >= 0)
);

-- Read-oriented indexes, mirroring EcoTrack-DDL.sql nonclustered indexes.
CREATE INDEX ix_employee_company_active
    ON employee (company_id, is_active) INCLUDE (employee_number, first_name, last_name, email, department);

CREATE INDEX ix_it_asset_company_type_status
    ON it_asset (company_id, asset_type, status) INCLUDE (asset_tag, host_name, manufacturer, model, serial_number);

CREATE INDEX ix_asset_assignment_employee_dates
    ON asset_assignment (employee_id, assigned_from, assigned_to) INCLUDE (asset_id, assignment_notes);

CREATE INDEX ix_asset_assignment_asset_dates
    ON asset_assignment (asset_id, assigned_from, assigned_to) INCLUDE (employee_id, assignment_notes);

CREATE INDEX ix_daily_consumption_asset_date
    ON daily_consumption (asset_id, consumption_date) INCLUDE (energy_kwh, carbon_kg_co2e, data_source, recorded_at);

CREATE INDEX ix_daily_consumption_date_asset
    ON daily_consumption (consumption_date, asset_id) INCLUDE (energy_kwh, carbon_kg_co2e, data_source);

-- Cross-table invariant not expressible as a standard CHECK constraint:
-- an assignment is only allowed for an active asset and an employee in the same company as the asset.
-- Enforced at insert/update time, matching AssetAssignmentManagementService.add in the Java domain.
CREATE FUNCTION enforce_asset_assignment_rules() RETURNS trigger AS
$$
DECLARE
    asset_status     varchar(20);
    asset_company_id integer;
    employee_company_id integer;
BEGIN
    SELECT status, company_id INTO asset_status, asset_company_id
    FROM it_asset WHERE asset_id = NEW.asset_id;

    SELECT company_id INTO employee_company_id
    FROM employee WHERE employee_id = NEW.employee_id;

    IF asset_status IS DISTINCT FROM 'ACTIVE' THEN
        RAISE EXCEPTION 'asset must be active to be assigned: %', NEW.asset_id
            USING ERRCODE = 'check_violation';
    END IF;

    IF asset_company_id IS DISTINCT FROM employee_company_id THEN
        RAISE EXCEPTION 'asset and employee must belong to the same company'
            USING ERRCODE = 'check_violation';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_asset_assignment_rules
    BEFORE INSERT OR UPDATE ON asset_assignment
    FOR EACH ROW
    EXECUTE FUNCTION enforce_asset_assignment_rules();
