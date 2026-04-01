import { configureStore, createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';
import {fetchTrips, fetchUser} from '../api/http.client';
import type { TripsResponse } from '../types/trip';
import {initialTripState, initialUserState} from "./defs.tsx";
import {User} from "../types/user.tsx";

interface LoadTripsArgs {
    pan: string;
    currentPage: number;
}
export const loadTrips = createAsyncThunk<TripsResponse, LoadTripsArgs>(
    'trips/load',
    (args) => fetchTrips(args.pan, args.currentPage),
);

export const loadUser = createAsyncThunk<User, String>(
    'user/load',
    (pan) => fetchUser(pan),
);

const tripsSlice = createSlice({
    name: 'trips',
    initialState: initialTripState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(loadTrips.pending, (s) => {
                s.loading = true;
                s.error = null;
            })
            .addCase(loadTrips.fulfilled, (s, { payload }) => {
                s.loading = false;
                s.items = payload.Trips;
                s.total = payload.Total;
                s.currentPage = payload.Page;
                s.pageSize = payload.PageSize;
                s.monthSpend = payload.MonthSpend;
                s.lifetimeSpend = payload.LifetimeSpend;
                s.thisMonthTrips = payload.MonthTripCount;
            })
            .addCase(loadTrips.rejected, (s, a) => {
                s.loading = false;
                s.error = a.error.message ?? 'Something went wrong. Please try again.';
            });
    },
});

const userSlice = createSlice({
    name: 'user',
    initialState: initialUserState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(loadUser.pending, (s) => {
                s.loading = true;
                s.error = null;
            })
            .addCase(loadUser.fulfilled, (s, { payload }) => {
                s.loading = false;
                s.pan = payload.PAN;
                s.name = payload.Name;
            })
            .addCase(loadUser.rejected, (s, a) => {
                s.loading = false;
                s.error = a.error.message ?? 'Something went wrong. Please try again.';
            });
    },
});

export const store = configureStore({
    reducer: {
        trips: tripsSlice.reducer,
        user: userSlice.reducer,
    },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;