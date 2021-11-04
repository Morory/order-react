import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import AuthService from "../service/auth.service";

export default function Home() {
    const user = AuthService.getCurrentUser();

    return (
        <Box display="flex" justifyContent="center" alignItems="center">
            <Card sx={{ minWidth: 275, maxWidth: 400 }}>
                <CardContent>
                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                        HOME
                    </Typography>
                    <Divider variant={"middle"}/>
                    <p>
                        <strong>Token:</strong>{" "}
                        {user.accessToken.substring(0, 20)} ...{" "}
                        {user.accessToken.substr(user.accessToken.length - 20)}
                    </p>
                    <p>
                        <strong>ID:</strong>{" "}
                        {user.id}
                    </p>
                    <p>
                        <strong>Username:</strong>{" "}
                        {user.username}
                    </p>
                    <p>
                        <strong>Email:</strong>{" "}
                        {user.email}
                    </p>
                    <strong>Authorities:</strong>
                    <ul>
                        {user.roles &&
                        user.roles.map((role, index) => <li key={index}>{role}</li>)}
                    </ul>
                </CardContent>
            </Card>
        </Box>
    );
}