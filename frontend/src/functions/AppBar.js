import {styled} from "@mui/material/styles";
import MuiAppBar from "@mui/material/AppBar";
import {jwtDecode} from "jwt-decode";

const drawerWidth = 240;

const token = localStorage.getItem('jwt');
const decodedToken = jwtDecode(token)
let color = ''
if(decodedToken.organization === 'Feuerwehr')
    color = '#C40C0C'
else
    color = '#0000ff'

const AppBar = styled(MuiAppBar, {
    shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
    }),
    backgroundColor: color,
    ...(open && {
        marginLeft: drawerWidth,
        width: `calc(100% - ${drawerWidth}px)`,
        transition: theme.transitions.create(['width', 'margin'], {
            easing: theme.transitions.easing.sharp,
            duration: theme.transitions.duration.enteringScreen,
        }),
    }),
}));

export default AppBar;