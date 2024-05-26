import {Box, Container, Grid, Toolbar, TextField, Button, Snackbar, Alert} from '@mui/material';
import {createTheme, ThemeProvider} from "@mui/material/styles";
import * as React from "react";
import {useState, useRef} from "react";
import Copyright from '../functions/Copyright';
import MenuBar from "../menu/MenuBar";
import {getTokenData} from "../functions/getTokenData";

const defaultTheme = createTheme();

export default function AdminCreateNewUser() {

    const { token, color } = getTokenData();

    const usernameRef = useRef(null);
    const firstNameRef = useRef(null);
    const lastNameRef = useRef(null);
    const passwordRef = useRef(null);

    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackbarSeverity, setSnackbarSeverity] = useState('success');

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    const handleUpdatePassword = async () => {
        const username = usernameRef.current.value;
        const firstName = firstNameRef.current.value;
        const lastName = lastNameRef.current.value;
        const password = passwordRef.current.value;

        if (!username || !firstName || !lastName || !password) {
            alert("Die Eingabefelder dürfen nicht leer sein!");
            return;
        }

        try {
            const response = await fetch(`http://localhost:9191/api/v1/users`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    user: {
                        username: username,
                        firstName: firstName,
                        lastName: lastName
                    },
                    password: password
                })
            });

            if (response.statusCode === 201) {
                console.error('Failed to create user: ', response.statusText);
                setSnackbarMessage(`Fehler beim Erstellen des Benutzers: ${response.statusText}`);
                setSnackbarSeverity('error');
                setSnackbarOpen(true);
                return;
            }

            usernameRef.current.value = '';
            firstNameRef.current.value = '';
            lastNameRef.current.value = '';
            passwordRef.current.value = '';

            setSnackbarMessage('Benutzer erfolgreich erstellt!');
            setSnackbarSeverity('success');
            setSnackbarOpen(true);
        } catch (error) {
            setSnackbarMessage(`Fehler beim Erstellen des Benutzers: ${error.message}`);
            setSnackbarSeverity('error');
            setSnackbarOpen(true);
        }
    };

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Neuen Benutzer erstellen"/>
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
                                    id="username"
                                    label="Benutzername"
                                    type="text"
                                    fullWidth
                                    autoComplete="Benutzername"
                                    inputRef={usernameRef}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    id="firstname"
                                    label="Vorname"
                                    type="text"
                                    fullWidth
                                    autoComplete="Vorname"
                                    inputRef={firstNameRef}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    id="lastname"
                                    label="Nachname"
                                    type="text"
                                    fullWidth
                                    autoComplete="Nachname"
                                    inputRef={lastNameRef}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    id="password"
                                    label="Passwort"
                                    type="text"
                                    fullWidth
                                    autoComplete="Passwort"
                                    inputRef={passwordRef}
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