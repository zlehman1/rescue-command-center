import React, { useEffect, useState } from 'react';
import { jwtDecode } from 'jwt-decode';
import { IconButton, Menu, MenuItem, Typography } from "@mui/material";
import { useNavigate } from 'react-router-dom';
import PersonIcon from '@mui/icons-material/Person';

const Profile = () => {
    const [anchorEl, setAnchorEl] = useState(null);
    const [timeRemaining, setTimeRemaining] = useState("");
    const navigate = useNavigate();

    const getToken = () => {
        const token = localStorage.getItem('jwt');
        if (token) {
            return jwtDecode(token);
        }
        return null;
    };
    const decodedToken = getToken();

    useEffect(() => {
        const calculateTimeLeft = () => {
            const now = Date.now() / 1000;
            if (decodedToken && decodedToken.exp) {
                return Math.max(0, decodedToken.exp - now);
            }
            return 0;
        };

        const updateTimer = () => {
            const secondsLeft = calculateTimeLeft();
            const formattedTime = `${Math.floor(secondsLeft / 3600)}h ${Math.floor((secondsLeft % 3600) / 60)}m ${Math.floor(secondsLeft % 60)}s`;
            setTimeRemaining(formattedTime);

            if (secondsLeft <= 0) {
                clearInterval(timer);
                localStorage.removeItem('jwt');
                navigate('/');
            }
        };

        const timer = setInterval(updateTimer, 1000);
        updateTimer();

        return () => clearInterval(timer);
    }, [decodedToken, navigate]);

    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleLogout = () => {
        localStorage.removeItem('jwt');
        navigate('/');
    };

    return (
        <div>
            <IconButton
                size="large"
                edge="end"
                color="inherit"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleMenu}
                sx={{ mr: 2 }}
            >
                <PersonIcon />
            </IconButton>
            <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleClose}
                TransitionProps={{ timeout: 350 }}
            >
                <MenuItem disabled>
                    <Typography>{decodedToken ? decodedToken.sub : 'Benutzer'}</Typography>
                </MenuItem>
                <MenuItem disabled>
                    <Typography>Session gültig: {timeRemaining}</Typography>
                </MenuItem>
                <MenuItem onClick={handleLogout}>Abmelden</MenuItem>
            </Menu>
        </div>
    );
};

export default Profile;