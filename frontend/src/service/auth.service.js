import http from "../util/http-common";

class AuthService {
    login(username, password) {
        return http.post("/auth/signin", {
            username,
            password
        })
    }

    register(username, email, password) {
        return http.post("/auth/signup", {
            username,
            email,
            password
        })
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }
}

export default new AuthService();