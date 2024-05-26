import React, { lazy, Suspense } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './functions/ProtectedRoute';
import SignIn from "./SignIn";

const Dashboard = lazy(() => import('./dashboard/Dashboard'));

const EmergencyCreation = lazy(() => import('./emergency/Create'));
const EmergencyOverview = lazy(() => import('./emergency/Overview'));
const EmergencyCallSingleView = lazy(() => import('./functions/EmergencyCallSingleView'));

const MapView = lazy(() => import('./Map/Map'));

const SettingDashboard = lazy(() => import('./settings/SettingDashboard'));
const PersonalSettings = lazy(() => import('./settings/PersonalSettings'));
const AdminSettings = lazy(() => import('./settings/AdminSettings'));
const AdminUsermanagement = lazy(() => import('./settings/AdminUsermanagement'));
const AdminRolemanagement = lazy(() => import('./settings/AdminRolemanagement'));
const AdminCreateNewUser = lazy(() => import('./settings/AdminCreateNewUser'));

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

                    <Route path="/settings/dashboard" element={<ProtectedRoute><SettingDashboard /></ProtectedRoute>} />
                    <Route path="/settings/personal" element={<ProtectedRoute><PersonalSettings /></ProtectedRoute>} />
                    <Route path="/settings/admin" element={<ProtectedRoute><AdminSettings /></ProtectedRoute>} />
                    <Route path="/settings/admin/users" element={<ProtectedRoute><AdminUsermanagement /></ProtectedRoute>} />
                    <Route path="/settings/admin/users/creation" element={<ProtectedRoute><AdminCreateNewUser /></ProtectedRoute>} />
                    <Route path="/settings/admin/roles" element={<ProtectedRoute><AdminRolemanagement /></ProtectedRoute>} />
                </Routes>
            </Suspense>
        </Router>
    );
}

export default App;
