import { useState, useEffect } from 'react';
import Papa from 'papaparse';

const useStichworte = () => {
    const [einsatzStichworte, setEinsatzStichworte] = useState([]);
    const [additionalStichworte, setAdditionalStichworte] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        async function fetchEinsatzData() {
            try {
                const response = await fetch('/einsatzstichworte/feuerwehr.csv');
                const text = await response.text();
                Papa.parse(text, {
                    complete: (results) => {
                        const stichworte = results.data.map(item => item[0]);
                        setEinsatzStichworte(stichworte.slice(1)); // Erste Zeile überspringen
                    },
                    header: false
                });
            } catch (error) {
                console.error("Fehler beim Laden der Einsatz-Stichworte: ", error);
            }
        }

        async function fetchAdditionalData() {
            try {
                const response = await fetch('/einsatzstichworte/polizei.csv');
                const text = await response.text();
                Papa.parse(text, {
                    complete: (results) => {
                        const stichworte = results.data.map(item => item[0]);
                        setAdditionalStichworte(stichworte.slice(1));
                    },
                    header: false
                });
            } catch (error) {
                console.error("Fehler beim Laden der zusätzlichen Stichworte: ", error);
            }
        }

        fetchEinsatzData();
        fetchAdditionalData().finally(() => setIsLoading(false)); // Setze isLoading auf false, wenn beide Anfragen abgeschlossen sind.
    }, []);

    return { einsatzStichworte, additionalStichworte, isLoading };
};

export default useStichworte;