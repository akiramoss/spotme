package com.spotme.spot.domain.repository;

import com.spotme.spot.domain.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {

}
