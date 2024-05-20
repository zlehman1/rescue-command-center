import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import SignIn from './SignIn';
import Dashboard from './dashboard/Dashboard';
import Map from './Map/Map';
import EmergencyCreation from './emergency/Create';
import EmergencyOverview from './emergency/Overview';
import ProtectedRoute from './functions/ProtectedRoute';
import EmergencyCallSingleView from "./functions/EmergencyCallSingleView";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Navigate replace to="/dashboard" />} />
                <Route path="/login" element={<SignIn />} />
                <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />

                <Route path="/map" element={<ProtectedRoute><Map /></ProtectedRoute>} />

                <Route path="/emergency/create" element={<ProtectedRoute><EmergencyCreation /></ProtectedRoute>} />
                <Route path="/emergency/overview" element={<ProtectedRoute><EmergencyOverview /></ProtectedRoute>} />
                <Route path="/emergency/detail" element={<ProtectedRoute><EmergencyCallSingleView /></ProtectedRoute>} />
            </Routes>
        </Router>
    );
}

export default App;
