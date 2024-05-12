import {useEffect, useState} from "react";
import {Typography} from "@mui/material";
import * as React from "react";

export default function DateTime() {
    const [currentTime, setCurrentTime] = useState(new Date());

    useEffect(() => {
        const timer = setInterval(() => {
            setCurrentTime(new Date());
        }, 1000);

        return () => clearInterval(timer);
    }, []);

    return (
        <Typography
            component="h1"
            variant="h6"
            color="inherit"
            noWrap
            sx={{ flexGrow: 0 }}
        >
             {currentTime.toLocaleTimeString('de-DE', { hour: '2-digit', minute: '2-digit', second: '2-digit' })} {currentTime.toLocaleDateString('de-DE')}
        </Typography>
    );
}