import { createRouter, createWebHistory } from 'vue-router';
import Dashboard from '../components/Dashboard/Dashboard.vue';
import Login from '../components/Login/Login.vue';
import EmergencyDashboard from "../components/emergency/EmergencyDashboard.vue";
import EmergencyForm from "../components/emergency/EmergencyForm.vue";
import EmergencyDetails from "../components/emergency/EmergencyDetails.vue";
import Map from "../components/Map/Map.vue";
import { useTokenData } from '../composables/useTokenData.js'

const requireAuth = (to, from, next) => {
    const token = localStorage.getItem('jwt');
    if (!token) {
        next({ name: 'Login' });
    } else {
        if (useTokenData().tokenValid.value){
            next();
        } else{
            localStorage.removeItem('jwt');
            localStorage.removeItem('emergencyData');
            next({ name: 'Login' });
        }
    }
};

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Dashboard,
        beforeEnter: requireAuth
    },
    {
        path: '/emergency/create',
        name: 'EmergencyForm',
        component: EmergencyForm,
        beforeEnter: requireAuth
    },
    {
        path: '/emergency/overview',
        name: 'EmergencyDashboard',
        component: EmergencyDashboard,
        beforeEnter: requireAuth
    },
    {
        path: '/emergency/details',
        name: 'EmergencyDetails',
        component: EmergencyDetails,
        beforeEnter: requireAuth
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/map',
        name: 'Map',
        component: Map,
        beforeEnter: requireAuth
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;