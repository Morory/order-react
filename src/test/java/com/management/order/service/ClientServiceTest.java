package com.management.order.service;

import com.management.order.model.Client;
import com.management.order.payload.request.SignupRequest;
import com.management.order.security.service.UserDetailsImpl;
import com.management.order.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ClientServiceTest {

    @Autowired
    private IAuthService authService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setup() {
        String username = "testName";
        String email = "testEmail@email.com";
        String password = "testPassword";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(username);
        signupRequest.setEmail(email);
        signupRequest.setPassword(password);

        authService.registerUser(signupRequest);

        userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
    }

    @Test
    void getAllByUser() {
        ResponseEntity<?> responseEntity = clientService.getAllByUser(userDetails);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void createClient() {
        Client client = Client.builder()
                .name("TestName")
                .address("TestAddress")
                .manager("TestManager")
                .tel("TestTel")
                .build();
        ResponseEntity<?> responseEntity = clientService.createClient(client, userDetails);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void updateClient() {
        String updateName = "UpdatedName";

        Client client = Client.builder()
                .name("TestName")
                .address("TestAddress")
                .manager("TestManager")
                .tel("TestTel")
                .build();
        Client savedClient = clientService.createClient(client, userDetails).getBody();
        savedClient.setName(updateName);
        Client updatedClient = clientService.updateClient(savedClient, userDetails).getBody();
        assertEquals(updateName, updatedClient.getName());
    }

    @Test
    void deletedClient() {
        Client client = Client.builder()
                .name("TestName")
                .address("TestAddress")
                .manager("TestManager")
                .tel("TestTel")
                .build();
        Client savedClient = clientService.createClient(client, userDetails).getBody();
        ResponseEntity<HttpStatus> responseEntity = clientService.deleteClient(savedClient.getId(), userDetails);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
