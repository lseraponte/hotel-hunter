package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.AmenityTranslation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AmenityRepositoryTest {

    @Autowired
    private AmenityRepository amenityRepository;

    private Amenity amenity;

    @BeforeEach
    void setUp() {

        AmenityTranslation translation = AmenityTranslation.builder()
                .name("Wi-Fi")
                .language("en")
                .build();

        amenity = Amenity.builder()
                .amenityId(1)
                .sort(1)
                .translations(List.of(translation))
                .build();

        amenityRepository.save(amenity);
    }

    @Test
    void testFindByAmenityId() {
        Optional<Amenity> foundAmenity = amenityRepository.findByAmenityId(1);

        assertTrue(foundAmenity.isPresent());
        assertEquals(1, foundAmenity.get().getAmenityId());
        assertEquals("Wi-Fi", foundAmenity.get().getTranslations().get(0).getName());
    }

    @Test
    void testFindByIdWithTranslationsByLanguage() {

        String language = "en";
        Optional<Amenity> foundAmenity = amenityRepository.findByIdWithTranslationsByLanguage(1, language);

        assertTrue(foundAmenity.isPresent());
        assertEquals(1, foundAmenity.get().getAmenityId());
        assertEquals("Wi-Fi", foundAmenity.get().getTranslations().get(0).getName());
        assertEquals(language, foundAmenity.get().getTranslations().get(0).getLanguage());
    }

    @Test
    void testFindByAmenityIdNotFound() {
        Optional<Amenity> foundAmenity = amenityRepository.findByAmenityId(999);

        assertFalse(foundAmenity.isPresent());
    }

    @Test
    void testFindByIdWithTranslationsByLanguageNotFound() {
        String language = "fr";
        Optional<Amenity> foundAmenity = amenityRepository.findByIdWithTranslationsByLanguage(1, language);

        assertFalse(foundAmenity.isPresent());
    }
}