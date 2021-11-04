import Box from "@mui/material/Box";
import React, { useState } from "react";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import Input from "@mui/material/Input";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import AuthService from "../service/auth.service";
import { useHistory } from "react-router-dom";

export default function Login() {
    const history = useHistory();
    const [username,setUsername] = useState('');
    const [email,setEmail] = useState('');
    const [password,setPassword] = useState('');

    const handleRegister = (e) => {
        e.preventDefault();

        AuthService.register(
            username,
            email,
            password
        ).then(
            response => {
                if(response.status === 200)
                    history.push('/login')
            },
            error => {
                alert(error.message)
            }
        )
    }

    const onChangeUsername = (e) => {
        setUsername(e.target.value);
    }
    const onChangeEmail = (e) => {
        setEmail(e.target.value);
    }
    const onChangePassword = (e) => {
        setPassword(e.target.value);
    }

        return (
            <Box display="flex" justifyContent="center" alignItems="center">
                <Card sx={{ minWidth: 275, maxWidth: 400 }}>
                    <CardContent>
                        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                            新規加入
                        </Typography>
                        <Divider variant={"middle"}/>
                        <form className={"form"} onSubmit={handleRegister}>
                            <div>
                                <Input
                                    type="text"
                                    placeholder={"ID"}
                                    onChange={onChangeUsername}
                                />
                            </div>
                            <div>
                                <Input
                                    type="email"
                                    placeholder={"EMAIL"}
                                    onChange={onChangeEmail}
                                />
                            </div>
                            <div>
                                <Input
                                    type="password"
                                    placeholder={"PW"}
                                    onChange={onChangePassword}
                                />
                            </div>
                            <Button type="submit" variant="contained">新規加入</Button>
                        </form>
                    </CardContent>
                </Card>
            </Box>
        );
}