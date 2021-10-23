import axios from "axios";

export default axios.create({
    baseURL: "/api",
    header: {
        "Content-type": "application/json; charset = utf-8"
    }
});