import type {TripsResponse} from "../types/trip.tsx";

interface TripsState {
    items: TripsResponse;
    total: number;
    loading: boolean;
    error: string | null;
}

export const initialTripState: TripsState = {
    items: [],
    total: 0,
    loading: false,
    error: null,
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