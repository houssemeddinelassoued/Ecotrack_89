export type AssetType = "LAPTOP" | "SERVER";

export type AssetStatus = "ACTIVE" | "INACTIVE" | "RETIRED" | "DISPOSED";

export interface AssetSummaryDto {
    assetId: string;
    assetTag: string;
    assetType: AssetType;
    displayName: string;
    status: AssetStatus;
    assignedEmployeeName: string | null;
    energyInKwh: string;
    carbonInKilogramsOfCo2e: string;
}

export interface DashboardSummaryDto {
    periodFrom: string;
    periodTo: string;
    energyInKwh: string;
    carbonInKilogramsOfCo2e: string;
    activeAssetCount: number;
    unassignedAssetCount: number;
    highestConsumers: AssetSummaryDto[];
}

export interface ApiErrorDto {
    code: "VALIDATION_ERROR" | "CONFLICT" | "FORBIDDEN" | "NOT_FOUND";
    message: string;
    fieldErrors?: Record<string, string>;
}

export interface CreateConsumptionRequest {
    assetId: string;
    consumptionDate: string;
    energyInKwh: string;
    dataSource?: string;
}

export function formatDecimalQuantity(value: string, unit: "kWh" | "kgCO2e", locale: string): string {
    const decimalParts = /^(\d+)(?:\.(\d{1,4}))?$/.exec(value);

    if (!decimalParts) {
        return `-- ${unit}`;
    }

    const integerPart = new Intl.NumberFormat(locale, { maximumFractionDigits: 0 })
        .format(BigInt(decimalParts[1]));
    const fractionPart = decimalParts[2]?.replace(/0+$/, "");
    const decimalSeparator = new Intl.NumberFormat(locale)
        .formatToParts(1.1)
        .find((part) => part.type === "decimal")?.value ?? ".";

    return `${integerPart}${fractionPart ? decimalSeparator + fractionPart : ""} ${unit}`;
}
