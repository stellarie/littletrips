import React from 'react';
import {
    Box,
    Typography
} from '@mui/material';
import DirectionsBusIcon from '@mui/icons-material/DirectionsBus';
import './error.page.scss';
import Header from "../components/header.component.tsx";
import {ArrowBack} from "@mui/icons-material";
import {useNavigate} from "react-router-dom";

const ErrorPage: React.FC = () => {
    const navigate = useNavigate();

    return (
        <Box className="err__root">
            <Header />
            <Box className="err__content">
                <Box className="err__empty">
                    <Box className="err__empty-icon-wrapper">
                        <DirectionsBusIcon className="err__empty-icon" />
                    </Box>
                    <Typography className="err__empty-title">This page does not exist!</Typography>
                    <Typography className="err__empty-description">
                        Maybe you entered an invalid URL?
                    </Typography>
                    <Box className="err__nav" onClick={()=>navigate("/")}>
                        <ArrowBack />&nbsp;
                        <Typography className="err__back-label">Search Passengers</Typography>
                    </Box>
                </Box>
            </Box>
        </Box>
    );
};

export default ErrorPage;