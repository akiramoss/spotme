package com.spotme.spot.repository;

import com.spotme.spot.domain.Category;
import com.spotme.spot.domain.Spot;
import com.spotme.user.domain.User;
import com.spotme.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.spotme.common.JpaAuditingConfig;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(JpaAuditingConfig.class)
class SpotRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void savesAndFindsSpotWithOwner() {
        User owner = userRepository.save(new User("owner@spotme.com", "testuser", "hashed_password"));

        Spot spot = new Spot(
                "Cozy cafe", "Great coffee and wifi",
                40.4168, -3.7038,
                Category.CAFE, owner,
                List.of("https://example.com/photo1.jpg")
        );
        spotRepository.save(spot);

        Optional<Spot> found = spotRepository.findById(spot.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Cozy cafe");
        assertThat(found.get().getOwner().getEmail()).isEqualTo("owner@spotme.com");
        assertThat(found.get().getImageUrls()).containsExactly("https://example.com/photo1.jpg");
    }
}

