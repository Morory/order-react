package com.management.order.controller;

import com.management.order.model.Client;
import com.management.order.security.service.UserDetailsImpl;
import com.management.order.service.IClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@Slf4j
@AllArgsConstructor
public class ClientController {

    private final IClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> getAllByUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Client List called");
        return clientService.getAllByUser(userDetails);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Client> createClient(@RequestBody Client client, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Client Create called");
        return clientService.createClient(client, userDetails);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Client> updateClient(@RequestBody Client client, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Client Update called");
        return clientService.updateClient(client, userDetails);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<HttpStatus> deleteClient(@PathVariable("id") long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Client Delete called");
        return clientService.deleteClient(id, userDetails);
    }

}
