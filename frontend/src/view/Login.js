import Box from "@mui/material/Box";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import Input from "@mui/material/Input";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import AuthService from "../service/auth.service";
import { useHistory } from "react-router-dom";
import {useState} from "react";

export default function Login() {
    const history = useHistory();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = (e) => {
        e.preventDefault();

        AuthService.login(
            username,
            password
        ).then(
            response => {
                if(response.status === 200) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                    history.push('/');
                }
            },
            error => {
                alert(error.message)
            }
        )

    }

    const onChangeUsername = (e) => {
        setUsername(e.target.value);
    }
    const onChangePassword = (e) => {
        setPassword(e.target.value);
    }

    return (
        <Box display="flex" justifyContent="center" alignItems="center">
            <Card sx={{ minWidth: 275, maxWidth: 400 }}>
                <CardContent>
                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                        ログイン
                    </Typography>
                    <Divider variant={"middle"}/>
                    <form className={"form"} onSubmit={handleLogin}>
                        <div>
                            <Input
                                type={"text"}
                                placeholder={"ID"}
                                onChange={onChangeUsername}
                            />
                        </div>
                        <div>
                            <Input
                                type={"password"}
                                placeholder={"PW"}
                                onChange={onChangePassword}
                            />
                        </div>
                        <Button type="submit" variant="contained">ログイン</Button>
                        <Button type="button" variant="contained" onClick={() => history.push('/register')}>新規加入</Button>
                    </form>
                </CardContent>
            </Card>
        </Box>
    );
}