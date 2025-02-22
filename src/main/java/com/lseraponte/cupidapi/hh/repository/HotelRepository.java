package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    Optional<Hotel> findByHotelId(int hotelId);

    // Fetch an Amenity along with its translations filtered by language
    @Query("SELECT h FROM Hotel h LEFT JOIN FETCH h.translations t WHERE h.hotelId = :hotelId AND t.language = :language")
    Optional<Hotel> findByIdWithTranslationsByLanguage(@Param("hotelId") Integer hotelId, @Param("language") String language);

}
