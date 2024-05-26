import {Box, Button, Container, Grid, Paper, Toolbar, Typography} from '@mui/material';
import {createTheme, ThemeProvider} from "@mui/material/styles";
import { DataGrid } from '@mui/x-data-grid';
import * as React from "react";
import {useNavigate} from "react-router-dom";
import {getTokenData} from "../functions/getTokenData";
import {useEffect, useState} from "react";
import { Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import Copyright from '../functions/Copyright'
import MenuBar from "../menu/MenuBar";

const defaultTheme = createTheme();

export default function AdminUsermanagement() {
    const navigate = useNavigate();
    const [data, setData] = useState([]);

    const { color, isAdmin, token } = getTokenData();

    if (!isAdmin) {
        navigate('/settings/dashboard');
    }

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch(`http://localhost:9191/api/v1/users`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                const result = await response.json();
                setData(result.user);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, [token]);

    const handleEdit = (username) => {
        console.log('Edit user:', username);
    };

    const handleDelete = (username) => {
        console.log('Delete user:', username);
    };

    const handleCreateUser = (username) => {
        navigate('creation')
    };

    const columns = [
        { field: 'username', headerName: 'Username', width: 150 },
        { field: 'firstName', headerName: 'Vorname', width: 150 },
        { field: 'lastName', headerName: 'Nachname', width: 150 },
        {
            field: 'actions',
            headerName: 'Aktion',
            width: 150,
            renderCell: (params) => (
                <div>
                    <Button onClick={() => handleEdit(params.row.username)} disabled={true}>
                        <EditIcon sx={{color: color}}  />
                    </Button>
                    <Button onClick={() => handleDelete(params.row.username)} disabled={true}>
                        <DeleteIcon sx={{color: color}} />
                    </Button>
                </div>
            ),
        },
    ];

    return (
        <ThemeProvider theme={defaultTheme}>
            <Box sx={{ display: 'flex' }}>
                <MenuBar title="Benutzerverwaltung"/>
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
                                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                                        <Typography variant="h6">Übersicht</Typography>
                                        <Button variant="contained" color="primary" sx={{backgroundColor: color}} onClick={() => handleCreateUser()} >
                                            Neuen Benutzer erstellen
                                        </Button>
                                    </Box>
                                    <div style={{ height: 400, width: '100%' }}>
                                        <DataGrid
                                            rows={data}
                                            columns={columns}
                                            pageSize={5}
                                            rowsPerPageOptions={[5]}
                                            getRowId={(row) => row.username}
                                        />
                                    </div>
                                </Paper>
                            </Grid>
                        </Grid>
                        <Copyright sx={{ pt: 4 }} />
                    </Container>
                </Box>
            </Box>
        </ThemeProvider>
    );
}