import {useEffect, useState} from "react";
import axios from "axios";
import {MapContainer, Marker, TileLayer} from "react-leaflet";
import * as React from "react";

const MapView = ({ address, height = 580, width = '100%', zoom = 17 }) => {
    const defaultPosition = [51.9377116, 7.1672926];
    const [position, setPosition] = useState(defaultPosition);

    useEffect(() => {
        if (address) {
            const apiKey = '8632dd4ed2ad48ccb6c52216742b6c88';
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
        <MapContainer center={position} zoom={zoom} style={{ height: height, width: width }} key={position}>
            <TileLayer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                errorTileUrl="https://via.placeholder.com/256?text=Error" // Placeholder für fehlgeschlagene Tiles
            />
            <Marker position={position} />
        </MapContainer>
    ) : null;
};

export default MapView;