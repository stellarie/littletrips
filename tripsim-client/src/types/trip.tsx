export type TripStatus = 'COMPLETED' | 'INCOMPLETE' | 'CANCELLED';

export interface Trip {
    Started: string;
    Finished: string | null;
    FromStopId: string;
    DurationSecs: number;
    ToStopId: string | null;
    ChargeAmount: number;
    PAN: string;
    routeName?: string;
    Status: TripStatus;
}

export interface TripsResponse extends Array<Trip> {
    [index: number]: Trip
}