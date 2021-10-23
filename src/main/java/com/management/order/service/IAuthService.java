package com.management.order.service;

import com.management.order.payload.request.LoginRequest;
import com.management.order.payload.request.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthService {

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest);

    public ResponseEntity<?> registerUser(SignupRequest signupRequest);
}
