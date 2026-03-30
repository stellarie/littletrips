import { Box, Button, Typography } from "@mui/material";
import {Trip} from "../types/trip.tsx";
import "./header.component.scss"
import {useNavigate} from "react-router-dom";

const Header = (props: {
    items?: Array<Trip>,
    monthSpend?: number,
    lifetimeSpend?: number,
    currency?: string,
    subtitle?: string,
}) => {
    const navigate = useNavigate();
    const { items, currency, monthSpend, subtitle } = props;
    return (
        <Box component="header" className="header">
            <Box className="header__logo-box">
                <Button onClick={():void => navigate("/")}>
                    <Typography className="header__logo-title">LittleTrips</Typography>
                </Button>
                <Typography className="header__logo-subtitle">{subtitle}</Typography>
            </Box>

            {items && items.length > 0 && (
                <Box className="header__month-spend">
                    <Typography className="header__month-spend-label">This Month's Spending</Typography>
                    <Typography className="header__month-spend-value">
                        {currency}&nbsp;{monthSpend && monthSpend.toFixed(2)}
                    </Typography>
                </Box>
            )}
        </Box>
    )
}

export default Header;