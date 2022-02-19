package com.postmission.repository;

import com.postmission.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email,String> {
    Optional<Email> findByEmailAddressAndEmailCheckToken(String emailAddress, String emailCheckToken);
}
