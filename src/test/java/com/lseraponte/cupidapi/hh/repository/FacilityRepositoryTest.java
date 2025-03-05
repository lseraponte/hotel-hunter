package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Facility;
import com.lseraponte.cupidapi.hh.model.FacilityTranslation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FacilityRepositoryTest {

    @Autowired
    private FacilityRepository facilityRepository;

    private Facility facility;

    @BeforeEach
    void setUp() {

        FacilityTranslation translation1 = FacilityTranslation.builder()
                .facilityName("Pool")
                .language("en")
                .build();
        FacilityTranslation translation2 = FacilityTranslation.builder()
                .facilityName("Piscina")
                .language("es")
                .build();

        facility = Facility.builder()
                .facilityId(1)
                .translations(List.of(translation1, translation2))
                .build();

        facilityRepository.save(facility);
    }

    @Test
    void testFindByFacilityId() {

        Optional<Facility> foundFacility = facilityRepository.findByFacilityId(facility.getFacilityId());

        assertTrue(foundFacility.isPresent());
        assertEquals(facility.getFacilityId(), foundFacility.get().getFacilityId());
    }

    @Test
    void testFindByIdWithTranslationsByLanguage() {

        Optional<Facility> foundFacilityEn = facilityRepository.findByIdWithTranslationsByLanguage(facility.getFacilityId(), "en");

        assertTrue(foundFacilityEn.isPresent());
        assertEquals("en", foundFacilityEn.get().getTranslations().get(0).getLanguage());
        assertEquals("Pool", foundFacilityEn.get().getTranslations().get(0).getFacilityName());

        Optional<Facility> foundFacilityFr = facilityRepository.findByIdWithTranslationsByLanguage(facility.getFacilityId(), "fr");

        assertFalse(foundFacilityFr.isPresent());
    }

    @Test
    void testFindByFacilityIdNotFound() {

        Optional<Facility> foundFacility = facilityRepository.findByFacilityId(999);

        assertFalse(foundFacility.isPresent());
    }

    @Test
    void testFindByIdWithTranslationsByLanguageNotFound() {

        Optional<Facility> foundFacility = facilityRepository.findByIdWithTranslationsByLanguage(1, "fr");

        assertFalse(foundFacility.isPresent());
    }
}
