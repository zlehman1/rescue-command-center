import useStichworte from "./useStichworte";
import {debounce} from "lodash";
import {Autocomplete, Button, Paper, TextField} from "@mui/material";
import * as React from "react";
import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function NotrufFormular({onAddressChange}) {
    const { einsatzStichworte, additionalStichworte } = useStichworte();
    const combinedStichworte = [...einsatzStichworte, ...additionalStichworte];

    const [keyword, setKeyword] = useState('');
    const [location, setLocation] = useState('');
    const [information, setInformation] = useState('');
    const [communicatorName, setCommunicatorName] = useState('');
    const [communicatorPhoneNumber, setCommunicatorPhoneNumber] = useState('');

    const navigate = useNavigate();

    const handleAddressChange = debounce((value) => {
        setLocation(value);
        onAddressChange(value);
    }, 300);

    const handleSubmit = async () => {
        const data = {
            keyword,
            location,
            information,
            communicatorName,
            communicatorPhoneNumber,
        };

        const token = localStorage.getItem('jwt');

        try {
            const response = await axios.post('http://localhost:9191/api/v1/emergency/fire', data, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            if (response.status === 200) {
                navigate('/emergency/overview');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

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
                color="primary"
                onClick={handleSubmit}
            >
                Notruf erstellen
            </Button>
        </Paper>
    );
}

export default NotrufFormular;