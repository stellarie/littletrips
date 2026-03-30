import React from 'react';
import { Box, Typography } from '@mui/material';
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';
import moment from 'moment';
import type { Trip } from '../types/trip';
import './trip-card.component.scss';

interface Props {
    trip: Trip;
}

const TripCard: React.FC<Props> = ({ trip }) => {
    const incomplete = trip.Status === 'INCOMPLETE';
    const cancelled = trip.Status === 'CANCELLED';

    // We pass the dynamic colors as CSS variables
    const accentColor = incomplete ? '#F97316' : '#06B6D4';
    const dynamicStyles = {
        '--accent-color': accentColor,
        '--accent-border': `${accentColor}33`,
        '--accent-bg': `${accentColor}0D`,
    } as React.CSSProperties;

    const fmt = (iso: string) => moment(iso).format('HH:mm');

    return (
        <Box className="tcc__container" style={dynamicStyles}>
            <Box className="tcc__accent-strip" />

            <Box className="tcc__content">
                <Box className="tcc__header">
                    <Box className="tcc__bus-info">
                        <DirectionsBusIcon className="tcc__bus-icon" />
                        <Typography className="tcc__bus-label">Bus</Typography>
                        {trip.routeName && (
                            <Typography className="tcc__route-name">
                                · {trip.routeName}
                            </Typography>
                        )}
                    </Box>

                    <Box className="tcc__status-pill">
                        <Typography className="tcc__status-text">
                            {incomplete ? 'No tap-off' : cancelled ? 'Cancelled' : 'Completed'}
                        </Typography>
                    </Box>
                </Box>

                <Box className="tcc__timeline">
                    <Box className="tcc__time-block">
                        <Typography className="tcc__time-val">
                            {fmt(trip.Started)}
                        </Typography>
                        <Typography className="tcc__stop-id">
                            {trip.FromStopId}
                        </Typography>
                    </Box>

                    <Box className="tcc__track">
                        <Box className="tcc__dot" />
                        <Box className={`tcc__line ${incomplete ? 'tcc__line--incomplete' : ''}`} />
                        <Box className={`tcc__dot ${incomplete ? 'tcc__dot--end-incomplete' : ''}`} />
                    </Box>

                    <Box className="tcc__time-block tcc__time-block--right">
                        <Typography className={`tcc__time-val ${incomplete ? 'tcc__time-val--muted' : ''}`}>
                            {trip.Finished ? fmt(trip.Finished) : '—\u2009—'}
                        </Typography>
                        <Typography className="tcc__stop-id">
                            {trip.ToStopId ?? 'Unknown stop'}
                        </Typography>
                    </Box>
                </Box>
            </Box>

            <Box className="tcc__divider">
                <Box className="tcc__divider-line" />
            </Box>

            <Box className="tcc__footer">
                <Typography className="tcc__fare">
                    AUD&nbsp;{trip.ChargeAmount.toFixed(2)}
                </Typography>
            </Box>
        </Box>
    );
};

export default TripCard;