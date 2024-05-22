import {createTheme, ThemeProvider} from "@mui/material/styles";
import {Box, Container, Grid, TextField, Toolbar} from "@mui/material";
import MenuBar from "../menu/MenuBar";
import Copyright from "../functions/Copyright";
import * as React from "react";
import MapView from "../functions/MapView";
import {useState} from "react";

import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
    iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
    iconUrl: require('leaflet/dist/images/marker-icon.png'),
    shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
});

const defaultTheme = createTheme();

export default function Map() {
    const [address, setAddress] = useState("");

    const handleAddressChange = (newAddress) => {
        setAddress(newAddress);
    };

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Karte" />
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
                                <TextField
                                    label="Ort"
                                    variant="outlined"
                                    fullWidth
                                    margin="normal"
                                    onChange={(e) => handleAddressChange(e.target.value)}
                                />
                            </Grid>
                            <MapView address={address} height={700} width="100%" zoom={18} />
                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}