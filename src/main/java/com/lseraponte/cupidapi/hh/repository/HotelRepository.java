package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    Optional<Hotel> findByHotelId(int hotelId);

}
