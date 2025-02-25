package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.dto.HotelWithTranslationDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query("SELECT new com.lseraponte.cupidapi.hh.dto.HotelWithTranslationDTO( " +
            "    h.hotelId, h.cupidId, t.hotelName, h.stars, t.description, t.markdownDescription, t.importantInfo, " +
            "    new com.lseraponte.cupidapi.hh.dto.AddressDTO(h.address.address, h.address.city, h.address.country), " +
            "    h.phone, h.fax, h.email, h.rating, h.reviewCount, " +
            "    h.checkinStart, h.checkinEnd, h.checkout, h.parking, " +
            "    h.childAllowed, h.petsAllowed, h.mainImageTh " +
            ") " +
            "FROM Hotel h " +
            "LEFT JOIN h.translations t " +
            "WHERE h.address.city = :city " +
            "AND t.language = :language")
    Optional<List<HotelWithTranslationDTO>> findHotelsWithTranslationsByCityAndLanguage(@Param("city") String city, @Param("language") String language);

    @Query("SELECT r FROM Hotel h JOIN h.reviews r WHERE h.hotelId = :hotelId")
    List<Review> findReviewsByHotelId(@Param("hotelId") Integer hotelId);

    @Query("SELECT r FROM Hotel h JOIN h.reviews r WHERE h.hotelId = :hotelId AND r.language = :language")
    List<Review> findReviewsByHotelIdAndLanguage(@Param("hotelId") Integer hotelId, @Param("language") String language);
}
