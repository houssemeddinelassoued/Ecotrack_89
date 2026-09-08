/*
    EcoTrack MVP - SQL Server relational model
    Scope: companies, employees, IT assets, asset assignments and daily consumption.
*/

IF DB_ID(N'EcoTrack') IS NULL
BEGIN
    EXEC (N'CREATE DATABASE [EcoTrack]');
END;
GO

USE [EcoTrack];
GO

SET ANSI_NULLS ON;
GO
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE dbo.Company
(
    CompanyId           int IDENTITY(1, 1) NOT NULL,
    LegalName           nvarchar(200) NOT NULL,
    RegistrationNumber  nvarchar(50) NULL,
    CountryCode         char(2) NOT NULL,
    IsActive            bit NOT NULL
        CONSTRAINT DF_Company_IsActive DEFAULT (1),
    CreatedAt           datetime2(0) NOT NULL
        CONSTRAINT DF_Company_CreatedAt DEFAULT (SYSUTCDATETIME()),

    CONSTRAINT PK_Company PRIMARY KEY CLUSTERED (CompanyId),
    CONSTRAINT UQ_Company_RegistrationNumber UNIQUE (RegistrationNumber),
    CONSTRAINT CK_Company_CountryCode CHECK (CountryCode LIKE '[A-Z][A-Z]')
);
GO

CREATE TABLE dbo.Employee
(
    EmployeeId      int IDENTITY(1, 1) NOT NULL,
    CompanyId       int NOT NULL,
    EmployeeNumber  nvarchar(50) NOT NULL,
    FirstName       nvarchar(100) NOT NULL,
    LastName        nvarchar(100) NOT NULL,
    Email           nvarchar(320) NOT NULL,
    Department      nvarchar(150) NULL,
    IsActive        bit NOT NULL
        CONSTRAINT DF_Employee_IsActive DEFAULT (1),
    CreatedAt       datetime2(0) NOT NULL
        CONSTRAINT DF_Employee_CreatedAt DEFAULT (SYSUTCDATETIME()),

    CONSTRAINT PK_Employee PRIMARY KEY CLUSTERED (EmployeeId),
    CONSTRAINT FK_Employee_Company FOREIGN KEY (CompanyId)
        REFERENCES dbo.Company (CompanyId),
    CONSTRAINT UQ_Employee_Company_EmployeeNumber
        UNIQUE (CompanyId, EmployeeNumber),
    CONSTRAINT UQ_Employee_Company_Email
        UNIQUE (CompanyId, Email)
);
GO

CREATE TABLE dbo.ITAsset
(
    AssetId         bigint IDENTITY(1, 1) NOT NULL,
    CompanyId       int NOT NULL,
    AssetTag        nvarchar(100) NOT NULL,
    AssetType       varchar(20) NOT NULL,
    HostName        nvarchar(255) NULL,
    Manufacturer    nvarchar(100) NULL,
    Model           nvarchar(150) NULL,
    SerialNumber    nvarchar(150) NULL,
    PurchaseDate    date NULL,
    Status          varchar(20) NOT NULL
        CONSTRAINT DF_ITAsset_Status DEFAULT ('Active'),
    CreatedAt       datetime2(0) NOT NULL
        CONSTRAINT DF_ITAsset_CreatedAt DEFAULT (SYSUTCDATETIME()),

    CONSTRAINT PK_ITAsset PRIMARY KEY CLUSTERED (AssetId),
    CONSTRAINT FK_ITAsset_Company FOREIGN KEY (CompanyId)
        REFERENCES dbo.Company (CompanyId),
    CONSTRAINT UQ_ITAsset_Company_AssetTag
        UNIQUE (CompanyId, AssetTag),
    CONSTRAINT CK_ITAsset_AssetType
        CHECK (AssetType IN ('Laptop', 'Server')),
    CONSTRAINT CK_ITAsset_Status
        CHECK (Status IN ('Active', 'Inactive', 'Retired', 'Disposed'))
);
GO

CREATE TABLE dbo.AssetAssignment
(
    AssignmentId      bigint IDENTITY(1, 1) NOT NULL,
    AssetId           bigint NOT NULL,
    EmployeeId        int NOT NULL,
    AssignedFrom      date NOT NULL,
    AssignedTo        date NULL,
    AssignmentNotes   nvarchar(500) NULL,

    CONSTRAINT PK_AssetAssignment PRIMARY KEY CLUSTERED (AssignmentId),
    CONSTRAINT FK_AssetAssignment_Asset FOREIGN KEY (AssetId)
        REFERENCES dbo.ITAsset (AssetId),
    CONSTRAINT FK_AssetAssignment_Employee FOREIGN KEY (EmployeeId)
        REFERENCES dbo.Employee (EmployeeId),
    CONSTRAINT CK_AssetAssignment_DateRange
        CHECK (AssignedTo IS NULL OR AssignedTo >= AssignedFrom)
);
GO

CREATE TABLE dbo.DailyConsumption
(
    ConsumptionId    bigint IDENTITY(1, 1) NOT NULL,
    AssetId          bigint NOT NULL,
    ConsumptionDate  date NOT NULL,
    EnergyKwh        decimal(12, 4) NOT NULL,
    CarbonKgCo2e     decimal(12, 4) NULL,
    DataSource       nvarchar(100) NULL,
    RecordedAt       datetime2(0) NOT NULL
        CONSTRAINT DF_DailyConsumption_RecordedAt DEFAULT (SYSUTCDATETIME()),

    CONSTRAINT PK_DailyConsumption PRIMARY KEY CLUSTERED (ConsumptionId),
    CONSTRAINT FK_DailyConsumption_Asset FOREIGN KEY (AssetId)
        REFERENCES dbo.ITAsset (AssetId),
    CONSTRAINT UQ_DailyConsumption_Asset_Date
        UNIQUE (AssetId, ConsumptionDate),
    CONSTRAINT CK_DailyConsumption_EnergyKwh CHECK (EnergyKwh >= 0),
    CONSTRAINT CK_DailyConsumption_CarbonKgCo2e
        CHECK (CarbonKgCo2e IS NULL OR CarbonKgCo2e >= 0)
);
GO

CREATE UNIQUE NONCLUSTERED INDEX UX_ITAsset_Company_SerialNumber
    ON dbo.ITAsset (CompanyId, SerialNumber)
    WHERE SerialNumber IS NOT NULL;
GO

/* Read-oriented nonclustered indexes. */
CREATE NONCLUSTERED INDEX IX_Employee_Company_Active
    ON dbo.Employee (CompanyId, IsActive)
    INCLUDE (EmployeeNumber, FirstName, LastName, Email, Department);
GO

CREATE NONCLUSTERED INDEX IX_ITAsset_Company_Type_Status
    ON dbo.ITAsset (CompanyId, AssetType, Status)
    INCLUDE (AssetTag, HostName, Manufacturer, Model, SerialNumber);
GO

CREATE NONCLUSTERED INDEX IX_AssetAssignment_Employee_Dates
    ON dbo.AssetAssignment (EmployeeId, AssignedFrom, AssignedTo)
    INCLUDE (AssetId, AssignmentNotes);
GO

CREATE NONCLUSTERED INDEX IX_AssetAssignment_Asset_Dates
    ON dbo.AssetAssignment (AssetId, AssignedFrom, AssignedTo)
    INCLUDE (EmployeeId, AssignmentNotes);
GO

CREATE NONCLUSTERED INDEX IX_DailyConsumption_Asset_Date
    ON dbo.DailyConsumption (AssetId, ConsumptionDate)
    INCLUDE (EnergyKwh, CarbonKgCo2e, DataSource, RecordedAt);
GO

CREATE NONCLUSTERED INDEX IX_DailyConsumption_Date_Asset
    ON dbo.DailyConsumption (ConsumptionDate, AssetId)
    INCLUDE (EnergyKwh, CarbonKgCo2e, DataSource);
GO

/* Deployment verification. */
DECLARE @ExpectedTableCount int = 5;
DECLARE @ExpectedForeignKeyCount int = 5;
DECLARE @ExpectedNonclusteredIndexCount int = 7;
DECLARE @ActualTableCount int;
DECLARE @ActualForeignKeyCount int;
DECLARE @ActualNonclusteredIndexCount int;

SELECT
    DB_NAME() AS DatabaseName,
    t.name AS TableName,
    SUM(p.rows) AS RowCount
FROM sys.tables AS t
INNER JOIN sys.partitions AS p
    ON p.object_id = t.object_id
   AND p.index_id IN (0, 1)
WHERE t.schema_id = SCHEMA_ID(N'dbo')
GROUP BY t.name;

SELECT
    i.name AS IndexName,
    OBJECT_SCHEMA_NAME(i.object_id) AS SchemaName,
    OBJECT_NAME(i.object_id) AS TableName,
    i.type_desc AS IndexType
FROM sys.indexes AS i
WHERE i.object_id IN
(
    OBJECT_ID(N'dbo.Company'),
    OBJECT_ID(N'dbo.Employee'),
    OBJECT_ID(N'dbo.ITAsset'),
    OBJECT_ID(N'dbo.AssetAssignment'),
    OBJECT_ID(N'dbo.DailyConsumption')
)
AND i.index_id > 0
ORDER BY TableName, i.index_id;

SELECT
    fk.name AS ForeignKeyName,
    OBJECT_SCHEMA_NAME(fk.parent_object_id) AS ParentTable,
    OBJECT_SCHEMA_NAME(fk.referenced_object_id) AS ReferencedSchema,
    OBJECT_NAME(fk.referenced_object_id) AS ReferencedTable
FROM sys.foreign_keys AS fk
WHERE fk.parent_object_id IN
(
    OBJECT_ID(N'dbo.Employee'),
    OBJECT_ID(N'dbo.ITAsset'),
    OBJECT_ID(N'dbo.AssetAssignment'),
    OBJECT_ID(N'dbo.DailyConsumption')
)
ORDER BY ParentTable, ForeignKeyName;

SELECT @ActualTableCount = COUNT(*)
FROM sys.tables
WHERE schema_id = SCHEMA_ID(N'dbo')
  AND name IN
  (
      N'Company', N'Employee', N'ITAsset',
      N'AssetAssignment', N'DailyConsumption'
  );

SELECT @ActualForeignKeyCount = COUNT(*)
FROM sys.foreign_keys
WHERE parent_object_id IN
(
    OBJECT_ID(N'dbo.Employee'),
    OBJECT_ID(N'dbo.ITAsset'),
    OBJECT_ID(N'dbo.AssetAssignment'),
    OBJECT_ID(N'dbo.DailyConsumption')
);

SELECT @ActualNonclusteredIndexCount = COUNT(*)
FROM sys.indexes AS i
WHERE i.type_desc = N'NONCLUSTERED'
  AND i.object_id IN
(
      OBJECT_ID(N'dbo.Company'),
      OBJECT_ID(N'dbo.Employee'),
      OBJECT_ID(N'dbo.ITAsset'),
      OBJECT_ID(N'dbo.AssetAssignment'),
      OBJECT_ID(N'dbo.DailyConsumption')
  );

IF DB_NAME() <> N'EcoTrack'
    THROW 51000, 'Verification failed: wrong database context.', 1;

IF @ActualTableCount <> @ExpectedTableCount
    THROW 51001, 'Verification failed: expected tables are missing.', 1;

IF @ActualForeignKeyCount <> @ExpectedForeignKeyCount
    THROW 51002, 'Verification failed: expected foreign keys are missing.', 1;

IF @ActualNonclusteredIndexCount <> @ExpectedNonclusteredIndexCount
    THROW 51003, 'Verification failed: expected nonclustered indexes are missing.', 1;

SELECT
    N'PASS' AS VerificationStatus,
    DB_NAME() AS DatabaseName,
    @ActualTableCount AS TableCount,
    @ActualForeignKeyCount AS ForeignKeyCount,
    @ActualNonclusteredIndexCount AS NonclusteredIndexCount;
GO
