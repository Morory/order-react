package com.management.order.controller;

import com.management.order.payload.request.SignupRequest;
import com.management.order.payload.request.LoginRequest;
import com.management.order.service.IAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("user signin called");
        return authService.authenticateUser(loginRequest);
        }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        log.info("user signup called");
        return authService.registerUser(signupRequest);
    }

}
