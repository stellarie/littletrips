import type {Trip, TripsResponse} from "../types/trip.tsx";

interface TripsState {
    trips: TripsResponse,
    items: Array<Trip>,
    total: number;
    loading: boolean;
    error: string | null;
    currentPage: number;
    pageSize: number;
    monthSpend: number;
    lifetimeSpend: number;
    thisMonthTrips: number;
}

export const initialTripState: TripsState = {
    trips: {
        Trips: [],
        Total: 0,
        Page: 0,
        PageSize: 0,
        MonthSpend: 0,
        LifetimeSpend: 0,
        MonthTripCount: 0
    },
    items: [],
    total: 0,
    loading: false,
    error: null,
    currentPage: 0,
    pageSize: 0,
    monthSpend: 0,
    lifetimeSpend: 0,
    thisMonthTrips: 0
};

interface UserState {
    pan: String,
    name: String,
    loading: boolean,
    error: string | null,
}

export const initialUserState: UserState = {
    pan: "",
    name: "",
    loading: true,
    error: null,
};