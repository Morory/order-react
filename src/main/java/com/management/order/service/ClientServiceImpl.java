package com.management.order.service;

import com.management.order.model.Client;
import com.management.order.repository.ClientRepository;
import com.management.order.security.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;

    @Override
    public ResponseEntity<List<Client>> getAllByUser(UserDetailsImpl userDetails) {
        try {
            List<Client> clients = new ArrayList<>();

            clientRepository.findAllByUserIdAndDeletedFalse(userDetails.getId()).forEach(clients::add);

            if(clients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Client> createClient(Client client, UserDetailsImpl userDetails) {
        try {
            Client _client = clientRepository
                    .save(Client.builder()
                            .name(client.getName())
                            .user(userDetails.getUser())
                            .address(client.getAddress())
                            .manager(client.getManager())
                            .tel(client.getTel())
                            .build());
            return new ResponseEntity<>(_client, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Client> updateClient(Client client, UserDetailsImpl userDetails) {
        try {
            Optional<Client> clientOptional = clientRepository.findById(client.getId());
            if (clientOptional.isPresent() && clientOptional.get().getUser().getId() == userDetails.getId()) {
                Client _client = clientOptional.get();
                _client.setName(client.getName());
                _client.setAddress(client.getAddress());
                _client.setManager(client.getManager());
                _client.setTel(client.getTel());
                _client.setBookmarked(client.isBookmarked());
                return new ResponseEntity<>(clientRepository.save(_client), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteClient(long id, UserDetailsImpl userDetails) {
        try {
            Optional<Client> clientOptional = clientRepository.findById(id);
            if (clientOptional.isPresent() && clientOptional.get().getUser().getId() == userDetails.getId()) {
                Client _client = clientOptional.get();
                _client.setDeleted(true);
                clientRepository.save(_client);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
