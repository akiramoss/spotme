package com.spotme.spot.domain;

import com.spotme.user.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SpotTest {

    private final User owner = new User("owner@spotme.com", "testuser", "hashed_password");

    @Test
    void rejectsMoreThanMaxImages() {
        List<String> tooManyImages = List.of(
                "url1", "url2", "url3", "url4", "url5", "url6"
        );

        assertThrows(IllegalArgumentException.class, () ->
                new Spot("Title", "Description", 1.0, 1.0, Category.CAFE, owner, tooManyImages)
        );
    }

    @Test
    void acceptsExactlyMaxImages() {
        List<String> exactlyFiveImages = List.of(
                "url1", "url2", "url3", "url4", "url5"
        );

        assertDoesNotThrow(() ->
                new Spot("Title", "Description", 1.0, 1.0, Category.CAFE, owner, exactlyFiveImages)
        );
    }

    @Test
    void allowsNoImages() {
        assertDoesNotThrow(() ->
                new Spot("Title", "Description", 1.0, 1.0, Category.CAFE, owner, null)
        );
    }
}
