package com.management.order.service;

import com.management.order.model.Client;
import com.management.order.security.service.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IClientService {

    public ResponseEntity<List<Client>> getAllByUser(UserDetailsImpl userDetails);

    public ResponseEntity<Client> createClient(Client client, UserDetailsImpl userDetails);

    public ResponseEntity<Client> updateClient(Client client, UserDetailsImpl userDetails);

    public ResponseEntity<HttpStatus> deleteClient(long id, UserDetailsImpl userDetails);
}
