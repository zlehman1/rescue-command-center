import {Box, Container, Grid, Toolbar, TextField, Button, Snackbar, Alert} from '@mui/material';
import {createTheme, ThemeProvider} from "@mui/material/styles";
import * as React from "react";
import {useState, useRef} from "react";
import Copyright from '../functions/Copyright';
import MenuBar from "../menu/MenuBar";
import {getTokenData} from "../functions/getTokenData";
import {useNavigate} from "react-router-dom";

const defaultTheme = createTheme();

export default function PersonalSettings() {

    const { token, color } = getTokenData();

    const navigate = useNavigate();

    const currentPasswordRef = useRef(null);
    const newPasswordRef = useRef(null);
    const repeatNewPasswordRef = useRef(null);

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    const handleUpdatePassword = async () => {
        const currentPassword = currentPasswordRef.current.value;
        const newPassword = newPasswordRef.current.value;
        const repeatNewPassword = repeatNewPasswordRef.current.value;

        if (!currentPassword || !newPassword || !repeatNewPassword) {
            alert("Die Eingabefelder dürfen nicht leer sein!");
            return;
        }

        if (newPassword !== repeatNewPassword) {
            alert("Das wiederholte Passwort ist nicht identisch!");
            return;
        }

        try {
            const response = await fetch(`http://localhost:9191/api/v1/users/password`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    currentPassword: currentPassword,
                    newPassword: newPassword
                })
            });

            if (!response.ok) {
                console.error('Failed to update password: ', response.statusText);
                setSnackbarMessage(`Fehler beim Aktualisieren des Passworts: ${response.statusText}`);
                setSnackbarSeverity('error');
                setSnackbarOpen(true);
                return;
            }

            currentPasswordRef.current.value = '';
            newPasswordRef.current.value = '';
            repeatNewPasswordRef.current.value = '';

            setSnackbarMessage('Passwort erfolgreich aktualisiert!');
            setSnackbarSeverity('success');
            setSnackbarOpen(true);

            setTimeout(() => {
                navigate('/settings/dashboard');
            }, 5000);

        } catch (error) {
            console.error('Error updating password', error);
            setSnackbarMessage(`Fehler beim Aktualisieren des Passworts: ${error.message}`);
            setSnackbarSeverity('error');
            setSnackbarOpen(true);
        }
    };

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Persönliche Einstellungen"/>
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
                                    required
                                    id="current-password"
                                    label="Aktuelles Passwort"
                                    type="password"
                                    fullWidth
                                    autoComplete="current-password"
                                    inputRef={currentPasswordRef}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    id="new-password"
                                    label="Neues Passwort"
                                    type="password"
                                    fullWidth
                                    autoComplete="new-password"
                                    inputRef={newPasswordRef}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    id="repeat-password"
                                    label="Passwort wiederholen"
                                    type="password"
                                    fullWidth
                                    autoComplete="new-password"
                                    inputRef={repeatNewPasswordRef}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    type="submit"
                                    sx={{backgroundColor: color}}
                                    onClick={handleUpdatePassword}
                                >
                                    Speichern
                                </Button>
                            </Grid>
                        </Grid>
                        <Snackbar
                            open={snackbarOpen}
                            autoHideDuration={6000}
                            onClose={handleSnackbarClose}
                        >
                            <Alert onClose={handleSnackbarClose} severity={snackbarSeverity} sx={{ width: '100%' }}>
                                {snackbarMessage}
                            </Alert>
                        </Snackbar>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}