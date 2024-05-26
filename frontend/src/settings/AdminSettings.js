import {Box, Container, Grid, Paper, Toolbar, Typography} from '@mui/material';

import {createTheme, ThemeProvider} from "@mui/material/styles";

import * as React from "react";

import Copyright from '../functions/Copyright'
import MenuBar from "../menu/MenuBar";
import PersonIcon from "@mui/icons-material/Person";
import LocalOfferIcon from '@mui/icons-material/LocalOffer';
import {useNavigate} from "react-router-dom";
import {getTokenData} from "../functions/getTokenData";

const defaultTheme = createTheme();

export default function AdminSettings() {
    const navigate = useNavigate();

    const { isAdmin } = getTokenData();

    if(!isAdmin){
        navigate('/settings/dashboard');
    }

    const handleUserManagementClick = () => {
        navigate('/settings/admin/users');
    };

    const handleRoleManagementClick = () => {
        navigate('/settings/admin/roles');
    };

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Administration"/>
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
                            <Grid item xs={12} sm={6} md={4} lg={3} key="personalSettings" onClick={() => handleUserManagementClick()}>
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
                                            <strong>Benutzerverwaltung</strong>
                                        </Typography>
                                    </Box>
                                    <Typography variant="body2" gutterBottom>
                                        Hier können Sie die Benutzer verwalten
                                    </Typography>
                                </Box>
                            </Grid>
                            <Grid item xs={12} sm={6} md={4} lg={3} key="adminSettings" onClick={() => handleRoleManagementClick()}>
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
                                        <LocalOfferIcon />
                                        <Typography variant="subtitle1" gutterBottom sx={{ marginLeft: 1 }}>
                                            <strong>Rollenverwaltung</strong>
                                        </Typography>
                                    </Box>
                                    <Typography variant="body2" gutterBottom>
                                        Hier können Sie die Rollen für die Benutzer verwalten
                                    </Typography>
                                </Box>
                            </Grid>
                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}