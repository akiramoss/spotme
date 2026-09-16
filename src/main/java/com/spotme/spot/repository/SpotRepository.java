package com.spotme.spot.repository;

import com.spotme.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findBySpotId(Long spotId);
}
