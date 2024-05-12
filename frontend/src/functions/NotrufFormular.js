import useStichworte from "./useStichworte";
import {debounce} from "lodash";
import {Autocomplete, Button, Paper, TextField} from "@mui/material";
import * as React from "react";

function NotrufFormular({onAddressChange}) {
    const { einsatzStichworte, additionalStichworte } = useStichworte();
    const combinedStichworte = [...einsatzStichworte, ...additionalStichworte];
    const handleAddressChange = debounce((value) => {
        onAddressChange(value);
    }, 300);

    return (
        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
            <Autocomplete
                options={combinedStichworte.sort()}
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
            />

            <TextField
                label="Name des Mitteilers"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <TextField
                label="Rufnummer des Mitteilers"
                variant="outlined"
                fullWidth
                margin="normal"
            />
            <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
            >
                Notruf erstellen
            </Button>
        </Paper>
    );
}

export default NotrufFormular;