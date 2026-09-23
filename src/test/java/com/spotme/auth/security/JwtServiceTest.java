package com.spotme.auth.security;

import com.spotme.user.domain.User;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    // 256-bit test secret — never used outside this test.
    private static final String TEST_SECRET =
            "0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcd";

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET);
        user = new User("test@spotme.com", "testuser", "hashed_password");
        setId(user, 42L);
    }

    @Test
    void generatesTokenWithCorrectClaims() {
        String token = jwtService.generateToken(user);

        assertThat(jwtService.extractUserId(token)).isEqualTo(42L);
        assertThat(jwtService.extractUsername(token)).isEqualTo("testuser");
    }

    @Test
    void rejectsMalformedToken() {
        assertThrows(JwtException.class, () -> jwtService.extractUserId("not.a.valid.token"));
    }

    @Test
    void rejectsTokenSignedWithDifferentSecret() {
        String differentSecret = "fedcba9876543210fedcba9876543210fedcba9876543210fedcba98765432";
        JwtService otherService = new JwtService(differentSecret);
        String tokenFromOtherService = otherService.generateToken(user);

        assertThrows(JwtException.class, () -> jwtService.extractUserId(tokenFromOtherService));
    }

    /**
     * Sets the private, JPA-managed id via reflection — User has no public
     * setter by design (id is assigned by the database), but tests need a
     * concrete id to assert against.
     */
    private void setId(User user, Long id) {
        try {
            var field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
