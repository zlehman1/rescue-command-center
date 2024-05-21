import {
    Box, Button, ButtonBase,
    Container,
    Grid,
    List,
    ListItem,
    ListItemText, TextField,
    Toolbar,
    Typography
} from "@mui/material";
import * as React from "react";
import {createTheme, ThemeProvider} from "@mui/material/styles";
import MenuBar from "../menu/MenuBar";
import Copyright from "./Copyright";
import { useLocation } from "react-router-dom";
import LocationOnIcon from '@mui/icons-material/LocationOn';
import InfoIcon from '@mui/icons-material/Info';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import WarningIcon from '@mui/icons-material/Warning';
import {jwtDecode} from "jwt-decode";
import MapView from "./MapView";
import {useEffect, useRef, useState} from "react";

const defaultTheme = createTheme();

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

const token = localStorage.getItem('jwt');
const decodedToken = jwtDecode(token);
let color = ''
let path = ''
if (decodedToken.organization === 'Feuerwehr'){
    color = '#C40C0C';
    path = 'fire'
}
else{
    color = '#0000ff';
    path = 'police';
}

export default function EmergencyCallSingleView(){
    const location = useLocation();
    const initialEmergencyData = useRef(location.state.emergencyData);
    const [emergencyData, setEmergencyData] = useState(location.state.emergencyData);

    const [mapHeight, setMapHeight] = useState(0);
    const gridRef = useRef(null);
    const messageRef = useRef(null);

    useEffect(() => {
        if (gridRef.current) {
            setMapHeight(gridRef.current.clientHeight);
        }
    }, [initialEmergencyData.current.value1]);

    const boxRef = useRef(null);

    useEffect(() => {
        if (boxRef.current) {
            boxRef.current.scrollTop = boxRef.current.scrollHeight;
        }
    }, [emergencyData.value1]);

    useEffect(() => {
        const handleRefresh = async () => {
            try {
                const jwt = localStorage.getItem('jwt');

                if (!jwt) {
                    console.error('JWT not found in localStorage');
                    return;
                }

                const response = await fetch(`http://localhost:9191/api/v1/emergency/${path}/${emergencyData.value0.id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${jwt}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    console.error('Failed to fetch emergency data', response.statusText);
                    return;
                }

                const data = await response.json();
                initialEmergencyData.current = data.data;
                setEmergencyData(data.data);  // Update state with the new data
            } catch (error) {
                console.error('Error fetching emergency data', error);
            }
        };

        window.addEventListener('load', handleRefresh);

        return () => {
            window.removeEventListener('load', handleRefresh);
        };
    }, [emergencyData]);

    const handleSendMessage = async () => {
        const message = messageRef.current.value;

        if (!message) {
            alert("Nachricht darf nicht leer sein!");
            return;
        }

        try {
            const jwt = localStorage.getItem('jwt');

            if (!jwt) {
                console.error('JWT not found in localStorage');
                return;
            }

            const response = await fetch(`http://localhost:9191/api/v1/emergency/${path}/message`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    emergencyId: emergencyData.value0.id,
                    message: message
                })
            });

            if (!response.ok) {
                console.error('Failed to send message', response.statusText);
                return;
            }

            window.location.reload();  // Reload the page after the message is sent
        } catch (error) {
            console.error('Error sending message', error);
        }
    };


    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Einsatzübersicht" />
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
                            <Grid item xs={12} md={6} sx={{ display: 'flex', flexDirection: 'column' }} ref={gridRef}>
                                <Box mb={2}>
                                    <Typography variant="h6" gutterBottom>
                                        <strong>Stichwort:</strong> {emergencyData.value0.keyword}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center" mb={1}>
                                    <LocationOnIcon sx={{ color: '#C40C0C' }} />
                                    <Typography variant="body2" gutterBottom sx={{ marginLeft: '8px' }}>
                                        {emergencyData.value0.location}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center" mb={1}>
                                    <InfoIcon sx={{ color: '#C40C0C' }} />
                                    <Typography variant="body2" gutterBottom sx={{ marginLeft: '8px' }}>
                                        {emergencyData.value0.information}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center" mb={1}>
                                    <WarningIcon sx={{ color: '#C40C0C' }} />
                                    <Typography variant="body2" gutterBottom sx={{ marginLeft: '8px' }}>
                                        {emergencyData.value0.emergencyCallState.emergencyCallStateEnum}
                                    </Typography>
                                </Box>
                                <Box display="flex" alignItems="center">
                                    <AccessTimeIcon sx={{ color: '#C40C0C' }} />
                                    <Typography variant="body2" sx={{ marginLeft: '8px' }}>
                                        {formatTimestamp(emergencyData.value0.timestamp)}
                                    </Typography>
                                </Box>
                            </Grid>
                            <Grid item xs={12} sx={{ mt: 4 }}>
                                <Typography variant="h6">Einsatzverlauf:</Typography>
                                <Box ref={boxRef} sx={{ maxHeight: 300, overflowY: 'auto', mb: 2 }}>
                                    <List>
                                        {emergencyData.value1.map((message) => (
                                            <ButtonBase
                                                key={message.id}
                                                sx={{ width: '100%', textAlign: 'left', '&:hover': { backgroundColor: 'rgba(0, 0, 0, 0.08)' } }}
                                            >
                                                <ListItem alignItems="flex-start">
                                                    <ListItemText
                                                        primary={message.text}
                                                        secondary={`${message.dispatcherName} - ${new Date(message.timestamp).toLocaleString()}`}
                                                    />
                                                </ListItem>
                                            </ButtonBase>
                                        ))}
                                    </List>
                                </Box>
                                <Box>
                                    <TextField
                                        multiline
                                        rows={4}
                                        variant="outlined"
                                        fullWidth
                                        placeholder="Neue Bemerkung..."
                                        sx={{ mb: 2 }}
                                        inputRef={messageRef}
                                    />
                                    <Button variant="contained" color="primary" onClick={handleSendMessage}>Absenden</Button>
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
