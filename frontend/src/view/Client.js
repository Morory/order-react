import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import FormDialog from "../component/FormDialog";
import {useEffect, useState} from "react";
import ClientService from "../service/client.service";
import IconButton from "@mui/material/IconButton";
import DeleteIcon from '@mui/icons-material/Delete';

export default function Client() {
    const [rows, setRows] = useState([]);

    useEffect(() => {
        console.log('mounted!');
        getClients();
    }, []);

    const getClients = () => {
        ClientService.getAll().then(
            response => {
                if(response.status === 200) {
                    setRows(response.data);
                }
            },
            error => {
                alert(error.message);
            });
    }

    const handleDelete = (rowId) => {
        ClientService.delete(rowId).then(
            response => {
                if(response.status === 200) {
                    getClients();
                }
            },
            error => {
                alert(error.message);
            }
        )
    }

    return (
        <div>
            <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                取引先<FormDialog/>
            </Typography>
            <Divider variant={"middle"}/>
            <TableContainer component={Paper}>
                <Table align="center" sx={{ minWidth: 400, maxWidth: 700 }} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="center">取引先名</TableCell>
                            <TableCell align="center">担当者名</TableCell>
                            <TableCell align="center">電話番号</TableCell>
                            <TableCell align="center">送り先</TableCell>
                            <TableCell align="center">操作</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows.map((row) => (
                            <TableRow
                                key={row.id}
                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                            >
                                <TableCell align="left">{row.name}</TableCell>
                                <TableCell align="left">{row.manager}</TableCell>
                                <TableCell align="center">{row.tel}</TableCell>
                                <TableCell align="left">{row.address}</TableCell>
                                <TableCell align="center">
                                    <IconButton onClick={() => handleDelete(row.id)}>
                                        <DeleteIcon/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    )
}