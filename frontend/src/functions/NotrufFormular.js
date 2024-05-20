import useStichworte from "./useStichworte";
import {debounce} from "lodash";
import {Autocomplete, Button, Paper, TextField} from "@mui/material";
import * as React from "react";
import {useState} from "react";
import axios from "axios";
import {Navigate, useNavigate} from "react-router-dom";
import {jwtDecode} from "jwt-decode";

function NotrufFormular({onAddressChange}) {
    const { einsatzStichworte, additionalStichworte } = useStichworte();
    const combinedStichworte = [...einsatzStichworte, ...additionalStichworte];

    const [keyword, setKeyword] = useState('');
    const [location, setLocation] = useState('');
    const [information, setInformation] = useState('');
    const [communicatorName, setCommunicatorName] = useState('');
    const [communicatorPhoneNumber, setCommunicatorPhoneNumber] = useState('');

    const [emergencyData, setEmergencyData] = useState(null);
    const [navigate, setNavigate] = useState(false);

    const handleAddressChange = debounce((value) => {
        setLocation(value);
        onAddressChange(value);
    }, 300);

    const token = localStorage.getItem('jwt');
    const decodedToken = jwtDecode(token)
    let color = ''
    if(decodedToken.organization === 'Feuerwehr')
        color = '#C40C0C'
    else
        color = '#0000ff'

    const handleSubmit = async () => {
        const data = {
            keyword,
            location,
            information,
            communicatorName,
            communicatorPhoneNumber,
        };

        try {
            let path = ''
            if(decodedToken.organization === 'Feuerwehr')
                path = 'fire'
            else
                path = 'police'

            const response = await axios.post(`http://localhost:9191/api/v1/emergency/${path}`, data, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            if (response.status === 200) {

                const response2 = await fetch(`http://localhost:9191/api/v1/emergency/${path}/${response.data.data.id}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                const responseData = await response2.json();
                setEmergencyData(responseData.data);
                setNavigate(true);
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    if (navigate && emergencyData) {
        return <Navigate to={`/emergency/detail`} state={{ emergencyData }} />;
    }

    return (
        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Autocomplete
                options={combinedStichworte.sort()}
                onChange={(event, value) => setKeyword(value)}
                renderInput={(params) => (
                    <TextField
                        {...params}
                        label="Einsatzstichwort"
                        margin="normal"
                    />
                )}
            />
            <TextField
                label="Ort"
                variant="outlined"
                fullWidth
                margin="normal"
                onChange={(e) => handleAddressChange(e.target.value)}
            />
            <TextField
                label="Einsatztext"
                variant="outlined"
                fullWidth
                margin="normal"
                multiline
                rows={6}
                onChange={(e) => setInformation(e.target.value)}
            />
            <TextField
                label="Name des Mitteilers"
                variant="outlined"
                fullWidth
                margin="normal"
                onChange={(e) => setCommunicatorName(e.target.value)}
            />
            <TextField
                label="Rufnummer des Mitteilers"
                variant="outlined"
                fullWidth
                margin="normal"
                onChange={(e) => setCommunicatorPhoneNumber(e.target.value)}
            />
            <Button
                fullWidth
                variant="contained"
                sx={{ backgroundColor: color }}
                onClick={handleSubmit}
            >
                Notruf erstellen
            </Button>
        </Paper>
    );
}

export default NotrufFormular;