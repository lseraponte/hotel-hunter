package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    Optional<Hotel> findByHotelId(int hotelId);

    @Query("SELECT h FROM Hotel h LEFT JOIN FETCH h.translations t WHERE h.hotelId = :hotelId AND t.language = :language")
    Optional<Hotel> findByIdWithTranslationsByLanguage(@Param("hotelId") Integer hotelId, @Param("language") String language);

    @Query("SELECT h FROM Hotel h LEFT JOIN FETCH h.translations t WHERE t.hotelName = :hotelName AND t.language = :language")
    Optional<Hotel> findByNameWithTranslationsByLanguage(@Param("hotelName") String hotelName, @Param("language") String language);

    @Query("SELECT r FROM Hotel h JOIN h.reviews r WHERE h.hotelId = :hotelId")
    List<Review> findReviewsByHotelId(@Param("hotelId") Integer hotelId);

    @Query("SELECT r FROM Hotel h JOIN h.reviews r WHERE h.hotelId = :hotelId AND r.language = :language")
    List<Review> findReviewsByHotelIdAndLanguage(@Param("hotelId") Integer hotelId, @Param("language") String language);
}
