import React, { useEffect, useMemo } from 'react';
import {
    Box,
    Typography,
    CircularProgress,
    Button,
    Skeleton
} from '@mui/material';
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';
import RefreshIcon from '@mui/icons-material/Refresh';
import moment from 'moment';
import {loadTrips, loadUser, useAppDispatch, useAppSelector} from '../store/store.tsx';
import TripCard from '../components/trip-card.component';
import type { Trip } from '../types/trip';
import './trip-history.page.scss';
import Header from "../components/header.component.tsx";
import {ArrowBack} from "@mui/icons-material";
import {useNavigate} from "react-router-dom";

function groupByDate(trips: Trip[]): { label: string; trips: Trip[] }[] {
    const groups: Record<string, Trip[]> = {};
    for (const t of trips) {
        const key = moment(t.Started).format('YYYY-MM-DD');
        (groups[key] ??= []).push(t);
    }
    const today = moment().startOf('day');
    return Object.entries(groups)
        .sort(([a], [b]) => b.localeCompare(a))
        .map(([date, trips]) => {
            const m = moment(date);
            let label: string;
            if (m.isSame(today, 'day')) label = 'Today';
            else if (m.isSame(today.clone().subtract(1, 'day'), 'day')) label = 'Yesterday';
            else label = m.format('ddd, D MMM YYYY');
            return { label, trips };
        });
}

const TripHistoryPage: React.FC = () => {
    const navigate = useNavigate();
    const params = new URLSearchParams(window.location.search);
    const pan = params.get('pan') || "";
    const appDispatch = useAppDispatch();
    const { items, loading, error, currentPage, total, pageSize, monthSpend, lifetimeSpend, thisMonthTrips } = useAppSelector((s) => s.trips);
    const { name } = useAppSelector((s) => s.user);

    useEffect(() => {
        appDispatch(loadTrips({pan, currentPage: 1}));
        appDispatch(loadUser(pan as String));
    }, []);

    const grouped = useMemo(() => groupByDate(items), [items]);
    const hasMore = pageSize*currentPage < total;

    return (
        <Box className="thp__root">
            <Header items={items} subtitle={"Trip History"} currency="AUD" lifetimeSpend={lifetimeSpend} />
            <Box className="thp__content">
                <Box className="thp__nav" onClick={()=>navigate("/")}>
                    <ArrowBack />&nbsp;
                    <Typography className="thp__back-label">Search</Typography>
                </Box>

                {name && (
                    <>
                        <Box className="thp__user-card">
                            <Box className="thp__user-card-content">
                                <Typography className="thp__card-label">Passenger Name</Typography>
                                <Typography className="thp__card-value">{ name }</Typography>
                            </Box>
                            <Box className="thp__user-card-content">
                                <Typography className="thp__card-label">PAN</Typography>
                                <Typography className="thp__card-value">{ pan }</Typography>
                            </Box>
                        </Box>
                        <Box className="thp__stats">
                            {[
                                { label: 'All time trips', value: total },
                                { label: 'Trips this month', value: thisMonthTrips },
                            ].map(({ label, value }) => (
                                <Box key={label} className="thp__stat-card">
                                    <Typography className="thp__stat-card-label">{label}</Typography>
                                    <Typography className="thp__stat-card-value">{value}</Typography>
                                </Box>
                            ))}
                        </Box>
                        <Box className="thp__stats">
                            {[
                                { label: 'Total Spent', value: `${lifetimeSpend}` },
                                { label: 'Spent This Month', value: `${monthSpend}` },
                            ].map(({ label, value }) => (
                                <Box key={label} className="thp__stat-card">
                                    <Typography className="thp__stat-card-label">{label}</Typography>
                                    <Typography className="thp__stat-card-value">{value}</Typography>
                                </Box>
                            ))}
                        </Box>
                    </>
                )}

                {error && (
                    <Box className="thp__error">
                        <Typography className="thp__error-title">Failed to load trips</Typography>
                        <Typography className="thp__error-message">{error}</Typography>
                        <Button
                            onClick={() => appDispatch(loadTrips({pan, currentPage: currentPage + 1}))}
                            startIcon={<RefreshIcon className="thp__refresh-icon" />}
                            className="thp__error-retry"
                        >
                            Retry
                        </Button>
                    </Box>
                )}

                {loading && items.length === 0 && (
                    <Box className="thp__skeletons">
                        <Box className="thp__skeleton-row">
                            {[1, 2].map((i) => (
                                <Skeleton
                                    key={i}
                                    variant="rectangular"
                                    height={88}
                                    className="thp__skeleton-stat"
                                />
                            ))}
                        </Box>
                        {[160, 160, 160, 160].map((h, i) => (
                            <Skeleton
                                key={i}
                                variant="rectangular"
                                height={h}
                                className="thp__skeleton-trip"
                            />
                        ))}
                    </Box>
                )}

                {!loading && !error && items.length === 0 && (
                    <Box className="thp__empty">
                        <Box className="thp__empty-icon-wrapper">
                            <DirectionsBusIcon className="thp__empty-icon" />
                        </Box>
                        <Typography className="thp__empty-title">No trips found for {pan}.</Typography>
                        <Typography className="thp__empty-description">
                            Maybe you entered an invalid PAN?
                        </Typography>
                    </Box>
                )}

                {
                    items.length > 0 && (
                        <Typography className="thp__history-label">Passenger History</Typography>
                    )
                }

                {grouped.map(({ label, trips }) => (
                    <Box key={label} className="thp__group">
                        <Typography className="thp__date-header">{label}</Typography>
                        <Box className="thp__cards">
                            {trips.map((trip, idx) => (
                                <TripCard key={idx} trip={trip} />
                            ))}
                        </Box>
                    </Box>
                ))}

                {!loading && (
                    <>
                        <Box sx={{ mt: 5, textAlign: 'center' }}>
                            {currentPage - 1 != 0 &&
                                <Button
                                    onClick={() => appDispatch(loadTrips({pan, currentPage: currentPage - 1}))}
                                    className="thp__load-more-btn"
                                >
                                    Previous Page
                                </Button>
                            }
                            {hasMore &&
                                <Button
                                    onClick={() => appDispatch(loadTrips({pan, currentPage: currentPage + 1}))}
                                    className="thp__load-more-btn"
                                >
                                    Next Page
                                </Button>
                            }
                        </Box>
                    </>
                )}

                {loading && items.length > 0 && (
                    <Box className="thp__spinner">
                        <CircularProgress size={22} className="thp__spinner-progress" />
                    </Box>
                )}
            </Box>
        </Box>
    );
};

export default TripHistoryPage;