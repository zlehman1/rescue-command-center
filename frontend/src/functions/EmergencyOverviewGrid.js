import { Grid, Paper, Typography, Box } from "@mui/material";
import * as React from "react";
import { useEffect, useState } from "react";
import {jwtDecode} from "jwt-decode";
import { Navigate } from 'react-router-dom';

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

export default function EmergencyOverviewGrid({ emergencies }) {
    const [selectedEmergency, setSelectedEmergency] = useState(null);
    const [emergencyData, setEmergencyData] = useState(null);
    const [navigate, setNavigate] = useState(false);

    const handleItemClick = (emergency) => {
        setSelectedEmergency(emergency);
    };

    useEffect(() => {
        if (selectedEmergency) {
            const fetchData = async () => {
                const token = localStorage.getItem('jwt');
                const decodedToken = jwtDecode(token);
                let path = decodedToken.organization === 'Feuerwehr' ? 'fire' : 'police';

                try {
                    const response = await fetch(`http://localhost:9191/api/v1/emergency/${path}/${selectedEmergency.id}`, {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    });
                    const data = await response.json();
                    setEmergencyData(data.data);
                    setNavigate(true);
                } catch (error) {
                    console.error('Error fetching data:', error);
                }
            };

            fetchData();
        }
    }, [selectedEmergency]);

    if (navigate && emergencyData) {
        return <Navigate to={`/emergency/detail`} state={{ emergencyData }} />;
    }

    return (
        <Paper sx={{ padding: 2 }}>
            <Grid container spacing={2}>
                {emergencies.length === 0 ? (
                    <Grid item xs={12}>
                        <Typography variant="subtitle1" gutterBottom>
                            Aktuell liegen keine Einsätze vor
                        </Typography>
                    </Grid>
                ) : (
                    emergencies.map((emergency) => (
                        <Grid item xs={12} sm={6} md={4} lg={3} key={emergency.id}>
                            <Box
                                component={Paper}
                                onClick={() => handleItemClick(emergency)}
                                sx={{
                                    padding: 2,
                                    height: '100%',
                                    cursor: 'pointer',
                                    '&:hover': {
                                        backgroundColor: '#f0f0f0',
                                    },
                                }}
                            >
                                <Typography variant="subtitle1" gutterBottom>
                                    <strong>Stichwort:</strong> {emergency.keyword}
                                </Typography>
                                <Typography variant="body2" gutterBottom>
                                    <strong>Ort:</strong> {emergency.location}
                                </Typography>
                                <Typography variant="body2" gutterBottom>
                                    <strong>Information:</strong> {emergency.information}
                                </Typography>
                                <Typography variant="body2" gutterBottom>
                                    <strong>Status:</strong> {emergency.emergencyCallState.emergencyCallStateEnum}
                                </Typography>
                                <Typography variant="body2">
                                    <strong>Zeit:</strong> {formatTimestamp(emergency.timestamp)}
                                </Typography>
                            </Box>
                        </Grid>
                    ))
                )}
            </Grid>
        </Paper>
    );
}