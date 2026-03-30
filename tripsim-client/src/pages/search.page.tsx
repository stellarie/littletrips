import React, { useState } from 'react';
import { Box, Typography, Button } from '@mui/material';
import './search.page.scss';
import Header from "../components/header.component.tsx";
import {useNavigate} from "react-router-dom";

const SearchPage: React.FC = () => {
    const navigate = useNavigate();
    const [userNumber, setUserNumber] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (userNumber.trim()) {
            navigate(`/trips/?pan=${userNumber}`)
        }
    };

    return (
        <>
            <Header />
            <Box className="search__wrapper">
                <Box className="search__card">
                    <Typography className="search__title">
                        Passenger History
                    </Typography>
                    <Typography className="search__subtitle">
                        Enter a passenger account number (PAN) to view their recent trip history and fare balances.
                    </Typography>

                    <form onSubmit={handleSubmit} className="search__input-group">
                        <Box className="search__input-wrapper">
                            <input
                                type="text"
                                className="search__input"
                                placeholder="5500001234567890"
                                value={userNumber}
                                onChange={(e) => setUserNumber(e.target.value)}
                            />
                        </Box>

                        <Button
                            type="submit"
                            className="search__button"
                            disabled={!userNumber.trim()}
                        >
                            {'View History'}
                        </Button>
                    </form>
                </Box>
            </Box>
        </>
    );
};

export default SearchPage;