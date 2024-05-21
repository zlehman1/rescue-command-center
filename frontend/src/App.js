import React, { lazy, Suspense } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './functions/ProtectedRoute';
import MapView from "./functions/MapView";
import SignIn from "./SignIn";

const Dashboard = lazy(() => import('./dashboard/Dashboard'));
const EmergencyCreation = lazy(() => import('./emergency/Create'));
const EmergencyOverview = lazy(() => import('./emergency/Overview'));
const EmergencyCallSingleView = lazy(() => import('./functions/EmergencyCallSingleView'));

function App() {
    return (
        <Router>
            <Suspense fallback={<div>Loading...</div>}>
                <Routes>
                    <Route path="/" element={<Navigate replace to="/dashboard" />} />
                    <Route path="/login" element={<SignIn />} />
                    <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />

                    <Route path="/map" element={<ProtectedRoute><MapView /></ProtectedRoute>} />
                    <Route path="/emergency/create" element={<ProtectedRoute><EmergencyCreation /></ProtectedRoute>} />
                    <Route path="/emergency/overview" element={<ProtectedRoute><EmergencyOverview /></ProtectedRoute>} />
                    <Route path="/emergency/detail" element={<ProtectedRoute><EmergencyCallSingleView /></ProtectedRoute>} />
                </Routes>
            </Suspense>
        </Router>
    );
}

export default App;
