package com.cardosofiles.nubank_challenge.domain.entity;

public class Contact {
    private Long id;
    private String email;
    private String phoneNumber;
    private Client client;

    public Contact(Long id, String email, String phoneNumber, Client client) {
        this.id = id;
        this.email = validateEmail(email);
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.client = validateClient(client);
    }

    private String validateEmail(String email) {
        if (email != null && email.length() > 100) {
            throw new IllegalArgumentException("Email cannot exceed 100 characters");
        }
        return email;
    }

    private String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() > 20) {
            throw new IllegalArgumentException("Phone number cannot exceed 20 characters");
        }
        return phoneNumber;
    }

    private Client validateClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Contact must be associated with a client");
        }
        return client;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = validateEmail(email);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = validatePhoneNumber(phoneNumber);
    }

    public Client getClient() {
        return client;
    }
}