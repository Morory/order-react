import http from "../http-common"
import authHeader from "@/services/auth-header";

class OrderService {
    getOrderNumber() {
        return http.get("/order/number", { headers: authHeader() });
    }

    getAll() {
        return http.get("/order", { headers: authHeader() });
    }

    getOrderWithOrderDetails(id) {
        return http.get("/order/" + id, { headers: authHeader() });
    }

    updateStatus(id) {
        return http.put("/order/status/" + id, {},{ headers: authHeader() });
    }

    update(data) {
        return http.put("/order", data, { headers: authHeader() });
    }

    create(data) {
        return http.post("/order", data, { headers: authHeader() });
    }

    deleteAll() {
        return http.delete("/order", { headers: authHeader() });
    }
}

export default new OrderService();