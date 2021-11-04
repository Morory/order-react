import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import Button from '@mui/material/Button';
import MenuIcon from '@mui/icons-material/Menu';
import { useHistory } from "react-router-dom";

export default function NavBar() {
    const history = useHistory();

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static" sx={{ mb: 5 }}>
                <Toolbar variant="dense">
                    <IconButton edge="start" color="inherit" aria-label="menu" sx={{ mr: 2 }}>
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" color="inherit" component="div">
                        受注管理システム
                    </Typography>
                    <Button
                        type="button"
                        variant="contained"
                        onClick={() => history.push('/client')}
                    >
                        取引先
                    </Button>
                    <Button
                        type="button"
                        variant="contained"
                        onClick={() => history.push('/order')}
                    >
                        受注管理
                    </Button>
                </Toolbar>
            </AppBar>
        </Box>
    );
}
