package com.cardosofiles.nubank_challenge_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactsResponseDTO {

    private Long id;
    private String email;
    private String phoneNumber;
}
