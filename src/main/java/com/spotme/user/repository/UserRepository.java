package com.spotme.user.repository;

import com.spotme.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional avoids NullPointerException
    Optional<User> findByEmail(String email);
}
