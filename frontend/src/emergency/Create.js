import { Box, Container, Grid, Toolbar } from '@mui/material';

import {createTheme, ThemeProvider} from "@mui/material/styles";

import * as React from "react";

import Copyright from '../functions/Copyright'
import {useState} from "react";
import MapView from '../functions/MapView'
import NotrufFormular from '../functions/NotrufFormular'

import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import MenuBar from "../menu/MenuBar";

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

const defaultTheme = createTheme();

export default function EmergencyCreation() {
    const [address, setAddress] = useState("");

    const handleAddressChange = (newAddress) => {
        setAddress(newAddress);
    };

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Notruf erfassen"/>
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
                        <Grid container spacing={3} justifyContent="center">
                            <Grid item xs={12} md={8} lg={6}>
                                <NotrufFormular onAddressChange={handleAddressChange} />
                            </Grid>
                            <Grid item xs={12} md={8} lg={6}>
                                <MapView address={address} />
                            </Grid>
                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}