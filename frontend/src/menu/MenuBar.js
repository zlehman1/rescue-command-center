import {CssBaseline, Divider, IconButton, List, Toolbar, Typography} from "@mui/material";
import AppBar from "../functions/AppBar";
import MenuIcon from "@mui/icons-material/Menu";
import DateTime from "../functions/DateTime";
import Profile from "../functions/Profile";
import Drawer from "../functions/Drawer";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import {mainListItems} from "./LeftSidebarMenu";
import * as React from "react";

export default function MenuBar({title = 'default menu title'}){
    const [open, setOpen] = React.useState(false);
    const toggleDrawer = () => {
        setOpen(!open);
    };

    return (
        <div>
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
                    <DateTime />
                    <Profile/>
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
        </div>
    )
}