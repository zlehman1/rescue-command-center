import {
    Box,
    Container,
    Grid,
    Toolbar,
} from '@mui/material';

import {createTheme, ThemeProvider} from "@mui/material/styles";

import * as React from "react";

import Copyright from '../functions/Copyright.js'
import MenuBar from "../menu/MenuBar";
import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import EmergencyOverviewGrid from "../functions/EmergencyOverviewGrid";

const defaultTheme = createTheme();

export default function EmergencyOverview() {
    const [emergencies, setEmergencies] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('jwt');
            const decodedToken = jwtDecode(token)
            let path = ''
            if(decodedToken.organization === 'Feuerwehr')
                path = 'fire'
            else
                path = 'police'

            try {
                const response = await fetch(`http://localhost:9191/api/v1/emergency/${path}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                const data = await response.json();
                setEmergencies(data.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Einsatzübersicht"/>
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
                            <Grid item xs={12}>
                                <EmergencyOverviewGrid emergencies={emergencies} />
                            </Grid>
                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}