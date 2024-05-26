import {jwtDecode} from 'jwt-decode';

export function getTokenData() {
    const token = localStorage.getItem('jwt');
    if (!token) {
        throw new Error('Token not found');
    }

    const decodedToken = jwtDecode(token);
    const roles = decodedToken.roles.map(role => role.name);
    const isDispatcher = roles.includes('DISPATCHER');
    const isAdmin = roles.includes('ADMIN');

    let color = '';
    let path = '';
    if (decodedToken.organization === 'Feuerwehr') {
        color = '#C40C0C';
        path = 'fire';
    } else {
        color = '#0000ff';
        path = 'police';
    }

    return {
        token,
        decodedToken,
        roles,
        isDispatcher,
        isAdmin,
        color,
        path
    };
}