import {Box, Container, Grid, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Toolbar} from '@mui/material';

import {createTheme, ThemeProvider} from "@mui/material/styles";

import * as React from "react";

import Copyright from '../functions/Copyright.js'
import MenuBar from "../menu/MenuBar";
import {useEffect, useState} from "react";

const defaultTheme = createTheme();

export default function EmergencyCreation() {
    const [emergencies, setEmergencies] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem('jwt');
            try {
                const response = await fetch('http://localhost:9191/api/v1/emergency/fire', {
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

    const formatTimestamp = (timestamp) => {
        const date = new Date(timestamp);
        const formattedDate = date.toLocaleDateString('de-DE', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
        const formattedTime = date.toLocaleTimeString('de-DE', {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
        return `${formattedTime} ${formattedDate}`;
    };

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
                                <TableContainer component={Paper}>
                                    <Table sx={{ minWidth: 650 }} aria-label="simple table">
                                        <TableHead>
                                            <TableRow>
                                                <TableCell align="right">Stichwort</TableCell>
                                                <TableCell align="right">Ort</TableCell>
                                                <TableCell align="right">Information</TableCell>
                                                <TableCell align="right">Status</TableCell>
                                                <TableCell align="right">Zeit</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {emergencies.map((emergency) => (
                                                <TableRow key={emergency.id}>
                                                    <TableCell align="right">{emergency.keyword}</TableCell>
                                                    <TableCell align="right">{emergency.location}</TableCell>
                                                    <TableCell align="right">{emergency.information}</TableCell>
                                                    <TableCell align="right">{emergency.emergencyCallState.emergencyCallStateEnum}</TableCell>
                                                    <TableCell align="right">{formatTimestamp(emergency.timestamp)}</TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </Grid>
                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}