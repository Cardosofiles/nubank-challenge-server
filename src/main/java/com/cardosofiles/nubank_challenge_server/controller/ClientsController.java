package com.cardosofiles.nubank_challenge_server.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cardosofiles.nubank_challenge_server.dto.ClientsDTO;
import com.cardosofiles.nubank_challenge_server.dto.ClientsResponseDTO;
import com.cardosofiles.nubank_challenge_server.services.ClientsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Validated
public class ClientsController {

    private final ClientsService clientsService;

    @PostMapping
    public ResponseEntity<ClientsResponseDTO> createClient(
            @Valid @RequestBody ClientsDTO clientDTO) {
        log.info("Requisição POST /api/clients - Cliente: {}", clientDTO.getName());
        ClientsResponseDTO response = clientsService.saveClient(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ClientsResponseDTO>> getAllClients() {
        log.info("Requisição GET /api/clients");
        List<ClientsResponseDTO> clients = clientsService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientsResponseDTO> getClientById(@PathVariable Long id) {
        log.info("Requisição GET /api/clients/{}", id);
        ClientsResponseDTO client = clientsService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("Requisição DELETE /api/clients/{}", id);
        clientsService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
