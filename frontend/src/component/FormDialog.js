import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {useState} from "react";
import ClientService from "../service/client.service";

export default function FormDialog() {
    const [open, setOpen] = React.useState(false);
    const [name, setName] = useState();
    const [manager, setManager] = useState();
    const [tel, setTel] = useState();
    const [address, setAddress] = useState();

    const onChangeName = (e) => {
        setName(e.target.value);
    }
    const onChangeManager = (e) => {
        setManager(e.target.value);
    }
    const onChangeTel = (e) => {
        setTel(e.target.value);
    }
    const onChangeAddress = (e) => {
        setAddress(e.target.value);
    }
    const handleClickOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };
    const handleCreate = () => {
        ClientService.create({
            name,
            manager,
            tel,
            address
        }).then(
            response => {
                if(response.status === 201) {
                    setOpen(false);
                    window.location.replace("/client");
                }
            },
            error => {
                alert(error.message)
            }
        )
    }

    return (
        <>
            <Button variant="outlined" onClick={handleClickOpen}>
                取引先の新規登録
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>取引先の新規登録</DialogTitle>
                <DialogContent>
                    <TextField
                        margin="dense"
                        label="取引先名"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={onChangeName}
                    />
                    <TextField
                        margin="dense"
                        label="担当者名"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={onChangeManager}
                    />
                    <TextField
                        margin="dense"
                        label="電話番号"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={onChangeTel}
                    />
                    <TextField
                        margin="dense"
                        label="送り先"
                        type="text"
                        fullWidth
                        variant="standard"
                        onChange={onChangeAddress}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>キャンセル</Button>
                    <Button onClick={handleCreate}>完了</Button>
                </DialogActions>
            </Dialog>
        </>
    );
}
