export type TripStatus = 'COMPLETED' | 'INCOMPLETE' | 'CANCELLED';

export interface Trip {
    Started: string;
    Finished: string | null;
    FromStopId: string;
    DurationSecs: number;
    ToStopId: string | null;
    ChargeAmount: number;
    PAN: string;
    Status: TripStatus;
}

export interface TripsResponse {
    Trips: Array<Trip>;
    Total: number;
    Page: number;
    PageSize: number;
    LifetimeSpend: number;
    MonthSpend: number;
    MonthTripCount: number;
}
