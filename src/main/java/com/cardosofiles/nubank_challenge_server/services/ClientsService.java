package com.cardosofiles.nubank_challenge_server.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cardosofiles.nubank_challenge_server.dto.ClientsDTO;
import com.cardosofiles.nubank_challenge_server.dto.ClientsResponseDTO;
import com.cardosofiles.nubank_challenge_server.dto.ContactsResponseDTO;
import com.cardosofiles.nubank_challenge_server.exception.ResourceNotFoundException;
import com.cardosofiles.nubank_challenge_server.model.Client;
import com.cardosofiles.nubank_challenge_server.model.Contact;
import com.cardosofiles.nubank_challenge_server.repositories.ClientsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientsService {

    private final ClientsRepository clientsRepository;

    @Transactional
    public ClientsResponseDTO saveClient(ClientsDTO clientDTO) {
        log.info("Salvando cliente: {}", clientDTO.getName());

        Client newClient = new Client();
        newClient.setName(clientDTO.getName());

        if (clientDTO.getContacts() != null && !clientDTO.getContacts().isEmpty()) {
            List<Contact> contacts = clientDTO.getContacts().stream()
                    .filter(contactDTO -> contactDTO.getEmail() != null
                            || contactDTO.getPhoneNumber() != null)
                    .map(contactDTO -> {
                        Contact contact = new Contact();
                        contact.setEmail(contactDTO.getEmail());
                        contact.setPhoneNumber(contactDTO.getPhoneNumber());
                        contact.setClient(newClient);
                        return contact;
                    }).collect(Collectors.toList());

            newClient.setContacts(contacts);
        }

        Client savedClient = clientsRepository.save(newClient);
        log.info("Cliente salvo com sucesso. ID: {}", savedClient.getId());

        return convertToResponseDTO(savedClient);
    }

    @Transactional(readOnly = true)
    public List<ClientsResponseDTO> getAllClients() {
        log.info("Buscando todos os clientes");
        return clientsRepository.findAll().stream().map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientsResponseDTO getClientById(Long id) {
        log.info("Buscando cliente ID: {}", id);
        Client client = clientsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
        return convertToResponseDTO(client);
    }

    @Transactional
    public void deleteClient(Long id) {
        log.info("Deletando cliente ID: {}", id);
        if (!clientsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
        }
        clientsRepository.deleteById(id);
        log.info("Cliente deletado com sucesso. ID: {}", id);
    }

    private ClientsResponseDTO convertToResponseDTO(Client client) {
        List<ContactsResponseDTO> contactsDTO = client.getContacts().stream()
                .map(contact -> new ContactsResponseDTO(contact.getId(), contact.getEmail(),
                        contact.getPhoneNumber()))
                .collect(Collectors.toList());

        return new ClientsResponseDTO(client.getId(), client.getName(), contactsDTO);
    }
}
