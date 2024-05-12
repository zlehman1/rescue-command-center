import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import SignIn from './SignIn';
import Dashboard from './dashboard/Dashboard';
import EmergencyCreation from './emergency/Create';
import EmergencyOverview from './emergency/Overview';
import ProtectedRoute from './functions/ProtectedRoute';

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<SignIn />} />
                <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
                <Route path="/emergency/create" element={<ProtectedRoute><EmergencyCreation /></ProtectedRoute>} />
                <Route path="/emergency/overview" element={<ProtectedRoute><EmergencyOverview /></ProtectedRoute>} />
                <Route path="/" element={<Navigate replace to="/dashboard" />} />
            </Routes>
        </Router>
    );
}

export default App;
