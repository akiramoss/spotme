package com.spotme.user.repository;

import com.spotme.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Looks up a user by email — used primarily during authentication
     * (see Beta 3: registration and login).
     */
    Optional<User> findByEmail(String email);
}
