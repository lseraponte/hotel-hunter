package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Integer> {

    Optional<Amenity> findByAmenityId(Integer amenityId);

    @Query("SELECT a FROM Amenity a LEFT JOIN FETCH a.translations t WHERE a.amenityId = :amenityId AND t.language = :language")
    Optional<Amenity> findByIdWithTranslationsByLanguage(@Param("amenityId") Integer amenityId, @Param("language") String language);

}
