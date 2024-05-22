import { CssBaseline, Divider, IconButton, List, Toolbar, Typography } from "@mui/material";
import AppBar from "../functions/AppBar";
import MenuIcon from "@mui/icons-material/Menu";
import DateTime from "../functions/DateTime";
import Profile from "../functions/Profile";
import Drawer from "../functions/Drawer";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import { mainListItems } from "./LeftSidebarMenu";
import * as React from "react";
import useMediaQuery from '@mui/material/useMediaQuery';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';

export default function MenuBar({ title = 'default menu title' }) {
    const [open, setOpen] = React.useState(false);
    const [darkMode, setDarkMode] = React.useState(false);

    const toggleDrawer = () => {
        setOpen(!open);
    };

    const toggleDarkMode = () => {
        setDarkMode(!darkMode);
    };

    const isMobile = useMediaQuery('(max-width:600px)');

    const theme = createTheme({
        palette: {
            mode: darkMode ? 'dark' : 'light',
        },
    });

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <AppBar position="absolute" open={open}>
                <Toolbar
                    sx={{
                        pr: '24px',
                    }}
                >
                    <IconButton
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        onClick={toggleDrawer}
                        sx={{
                            marginRight: '36px',
                            ...(open && { display: 'none' }),
                        }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography
                        component="h1"
                        variant="h6"
                        color="inherit"
                        noWrap
                        sx={{ flexGrow: 1 }}
                    >
                        {title}
                    </Typography>
                    {!isMobile && <DateTime />}
                    <IconButton
                        edge="end"
                        color="inherit"
                        aria-label="toggle dark mode"
                        onClick={toggleDarkMode}
                    >
                        {darkMode ? <Brightness7Icon /> : <Brightness4Icon />}
                    </IconButton>
                    <Profile />
                </Toolbar>
            </AppBar>
            <Drawer variant="permanent" open={open}>
                <Toolbar
                    sx={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'flex-end',
                        px: [1],
                    }}
                >
                    <IconButton onClick={toggleDrawer}>
                        <ChevronLeftIcon />
                    </IconButton>
                </Toolbar>
                <Divider />
                <List component="nav">
                    {mainListItems}
                </List>
            </Drawer>
        </ThemeProvider>
    );
}
