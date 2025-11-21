package com.cardosofiles.nubank_challenge_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cardosofiles.nubank_challenge_server.model.Contact;

@Repository
public interface ContactsRepository extends JpaRepository<Contact, Long> {
}
