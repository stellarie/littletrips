import axios from 'axios';
import type { TripsResponse } from '../types/trip';
import {User} from "../types/user.tsx";

const api = axios.create({
    baseURL: '/api',
    headers: { 'Content-Type': 'application/json' },
});

export const fetchTrips = (pan: String): Promise<TripsResponse> =>
    api.get<TripsResponse>(`/trips/${pan}/history`).then((r) => r.data);

export const fetchUser = (pan: String): Promise<User> =>
    api.get<User>(`/user/${pan}`).then((r) => r.data);