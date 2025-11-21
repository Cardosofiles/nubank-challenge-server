package com.cardosofiles.nubank_challenge_server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactsDTO {

    @Email(message = "Email inválido")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{9,14}$", message = "Telefone inválido (mínimo 10 dígitos)")
    private String phoneNumber;
}
