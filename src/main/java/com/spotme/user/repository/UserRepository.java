package com.spotme.user.repository;

import com.spotme.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Looks up a user by email — used primarily during authentication
     * @param email
     * @return
     */
    Optional<User> findByEmail(String email);

    /**
     * Looks up a user by username — used primarily during authentication
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);
}
