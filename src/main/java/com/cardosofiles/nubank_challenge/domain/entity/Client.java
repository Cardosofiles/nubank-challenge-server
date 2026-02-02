package com.cardosofiles.nubank_challenge.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private Long id;
    private String name;
    private List<Contact> contacts;

    public Client(Long id, String name) {
        this.id = id;
        this.name = validateName(name);
        this.contacts = new ArrayList<>();
    }

    public Client(Long id, String name, List<Contact> contacts) {
        this.id = id;
        this.name = validateName(name);
        this.contacts = contacts != null ? contacts : new ArrayList<>();
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Client name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Client name cannot exceed 100 characters");
        }
        return name;
    }

    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null");
        }
        this.contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validateName(name);
    }

    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }
}