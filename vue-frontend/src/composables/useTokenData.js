import { ref } from 'vue';
import {jwtDecode} from 'jwt-decode';

export function useTokenData() {
    const token = ref(localStorage.getItem('jwt'));
    const decodedToken = ref(null);
    const username = ref(null);
    const roles = ref([]);
    const isDispatcher = ref(false);
    const isAdmin = ref(false);
    const color = ref('');
    const path = ref('');
    const organization = ref('');
    const tokenValid = ref(false);

    if (!token.value) {
        throw new Error('Token not found');
    }

    try {
        decodedToken.value = jwtDecode(token.value);
        roles.value = decodedToken.value.roles.map(role => role.name);
        isDispatcher.value = roles.value.includes('DISPATCHER');
        isAdmin.value = roles.value.includes('ADMIN');
        username.value = decodedToken.value.sub;
        organization.value = decodedToken.value.organization;

        const currentTime = Date.now() / 1000;
        tokenValid.value = currentTime < decodedToken.value.exp;

        if (decodedToken.value.organization === 'Feuerwehr') {
            color.value = '#C40C0C';
            path.value = 'fire';
        } else {
            color.value = '#0000ff';
            path.value = 'police';
        }
    } catch (error) {
        console.error('Error decoding token:', error);
        throw new Error('Invalid token');
    }

    return {
        token,
        decodedToken,
        roles,
        isDispatcher,
        isAdmin,
        color,
        path,
        username,
        organization,
        tokenValid
    };
}