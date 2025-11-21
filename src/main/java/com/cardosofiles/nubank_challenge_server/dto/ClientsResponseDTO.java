package com.cardosofiles.nubank_challenge_server.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientsResponseDTO {

    private long id;
    private String name;
    private List<ContactsResponseDTO> contacts;

}
