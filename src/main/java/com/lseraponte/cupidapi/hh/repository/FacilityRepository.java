package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {

    Optional<Facility> findByFacilityId(Integer facilityId);

    @Query("SELECT f FROM Facility f LEFT JOIN FETCH f.translations t WHERE f.facilityId = :facilityId AND t.language = :language")
    Optional<Facility> findByIdWithTranslationsByLanguage(@Param("facilityId") Integer facilityId, @Param("language") String language);
}
