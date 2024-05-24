import { useState, useEffect } from 'react';
import Papa from 'papaparse';
import {jwtDecode} from 'jwt-decode';

const useKeyword = (token) => {
    const [keywords, setKeywords] = useState([]);
    const decodedToken = jwtDecode(token);
    const organization = decodedToken.organization;

    useEffect(() => {
        const fetchData = async (csvFile) => {
            try {
                const response = await fetch(csvFile);
                const text = await response.text();
                Papa.parse(text, {
                    complete: (results) => {
                        const keywords = results.data.map(item => item[0]);
                        setKeywords(keywords.slice(1));
                    },
                    header: false
                });
            } catch (error) {
                console.error("Fehler beim Laden der Stichworte: ", error);
            }
        };

        if (organization === 'Feuerwehr') {
            fetchData('/einsatzstichworte/feuerwehr.csv');
        } else if (organization === 'Polizei') {
            fetchData('/einsatzstichworte/polizei.csv');
        } else {
            console.error("Unbekannte Organisation: ", organization);
        }
    }, [organization]);

    return { keywords };
};

export default useKeyword;