import axios from 'axios';
import type { TripsResponse } from '../types/trip';
import {User} from "../types/user.tsx";
import {CONFIG} from "../app.config.tsx";

const api = axios.create({
    baseURL: '/api',
    headers: { 'Content-Type': 'application/json' },
});

export const fetchTrips = (pan: String, page: number = 0): Promise<TripsResponse> =>
    api.get<TripsResponse>(`/trips/${pan}/history?page=${page}&size=${CONFIG.page_size}`).then((r) => r.data);

export const fetchUser = (pan: String): Promise<User> =>
    api.get<User>(`/user/${pan}`).then((r) => r.data);