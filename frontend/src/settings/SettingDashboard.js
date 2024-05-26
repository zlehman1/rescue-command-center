import {Box, Container, Grid, Paper, Toolbar, Typography} from '@mui/material';

import {createTheme, ThemeProvider} from "@mui/material/styles";

import * as React from "react";

import { useNavigate } from 'react-router-dom';

import Copyright from '../functions/Copyright'
import MenuBar from "../menu/MenuBar";
import PersonIcon from '@mui/icons-material/Person';
import SettingsIcon from '@mui/icons-material/Settings';
import {getTokenData} from "../functions/getTokenData";

const defaultTheme = createTheme();

export default function SettingDashboard() {
    const navigate = useNavigate();
    const { isAdmin } = getTokenData();

    const handlePersonalSettingsClick = () => {
        navigate('/settings/personal');
    };

    const handleAdminSettingsClick = () => {
        navigate('/settings/admin');
    };

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Einstellungen"/>
                <Box
                    component="main"
                    sx={{
                        backgroundColor: (theme) =>
                            theme.palette.mode === 'light'
                                ? theme.palette.grey[100]
                                : theme.palette.grey[900],
                        flexGrow: 1,
                        height: '100vh',
                        overflow: 'auto',
                    }}
                >
                    <Toolbar />
                    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                        <Grid container spacing={3}>
                            <Grid item xs={12} sm={6} md={4} lg={3} key="personalSettings" onClick={() => handlePersonalSettingsClick()}>
                                <Box
                                    component={Paper}

                                    sx={{
                                        padding: 2,
                                        height: '150px',
                                        cursor: 'pointer',
                                        '&:hover': {
                                            backgroundColor: '#f0f0f0',
                                        },
                                    }}
                                >
                                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                        <PersonIcon />
                                        <Typography variant="subtitle1" gutterBottom sx={{ marginLeft: 1 }}>
                                            <strong>Persönliche Einstellungen</strong>
                                        </Typography>
                                    </Box>
                                    <Typography variant="body2" gutterBottom>
                                        Hier können Sie ihr Passwort ändern
                                    </Typography>
                                </Box>
                            </Grid>
                            {
                                isAdmin && (
                                    <Grid item xs={12} sm={6} md={4} lg={3} key="adminSettings" onClick={() => handleAdminSettingsClick()} >
                                        <Box
                                            component={Paper}

                                            sx={{
                                                padding: 2,
                                                height: '150px',
                                                cursor: 'pointer',
                                                '&:hover': {
                                                    backgroundColor: '#f0f0f0',
                                                },
                                            }}
                                        >
                                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                                <SettingsIcon />
                                                <Typography variant="subtitle1" gutterBottom sx={{ marginLeft: 1 }}>
                                                    <strong>Administration</strong>
                                                </Typography>
                                            </Box>
                                            <Typography variant="body2" gutterBottom>
                                                Hier können Sie administrative Einstellungen vornehmen
                                            </Typography>
                                        </Box>
                                    </Grid>
                                )
                            }

                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}