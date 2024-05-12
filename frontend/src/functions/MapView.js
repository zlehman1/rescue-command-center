import {useEffect, useState} from "react";
import axios from "axios";
import {MapContainer, Marker, TileLayer} from "react-leaflet";
import * as React from "react";

const MapView = ({ address }) => {
    const defaultPosition = [51.9377116, 7.1672926];
    const [position, setPosition] = useState(defaultPosition);

    useEffect(() => {
        if (address) {
            const apiKey = '849980996329428785d88960dd5ae716';
            const requestUrl = `https://api.opencagedata.com/geocode/v1/json?q=${encodeURIComponent(address)}&key=${apiKey}`;

            axios.get(requestUrl)
                .then(response => {
                    const results = response.data.results;
                    if (results.length > 0) {
                        const { lat, lng } = results[0].geometry;
                        setPosition([lat, lng]);
                    } else {
                        console.log('Keine Ergebnisse gefunden');
                    }
                })
                .catch(error => {
                    console.error('Fehler beim Geocoding:', error.message);
                });
        }
    }, [address]);

    return position ? (
        <MapContainer center={position} zoom={17} style={{ height: 580, width: '100%' }} key={position}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            />
            <Marker position={position} />
        </MapContainer>
    ) : null;
};

export default MapView;