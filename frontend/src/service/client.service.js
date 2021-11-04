import http from "../util/http-common";
import authHeader from "./auth-header";

class ClientService {
    getAll() {
        return http.get("/client", { headers: authHeader() });
    }

    create(data) {
        return http.post("/client", data, { headers: authHeader() });
    }

    update(data) {
        return http.put("/client", data, { headers: authHeader() });
    }

    delete(id) {
        return http.delete("/client/" + id, { headers: authHeader() });
    }
}

export default new ClientService();