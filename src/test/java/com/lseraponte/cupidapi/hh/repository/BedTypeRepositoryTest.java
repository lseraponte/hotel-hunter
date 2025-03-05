package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.BedType;
import com.lseraponte.cupidapi.hh.model.BedTypeTranslation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BedTypeRepositoryTest {

    @Autowired
    private BedTypeRepository bedTypeRepository;

    private BedType bedType;

    @BeforeEach
    void setUp() {

        BedTypeTranslation translation1 = BedTypeTranslation.builder()
                .bedTypeName("King")
                .bedSize("Large")
                .language("en")
                .build();
        BedTypeTranslation translation2 = BedTypeTranslation.builder()
                .bedTypeName("King")
                .bedSize("Large")
                .language("fr")
                .build();

        bedType = BedType.builder()
                .quantity(2)
                .translations(List.of(translation1, translation2))
                .build();

        bedTypeRepository.save(bedType);
    }

    @Test
    void testFindByBedTypeName() {
        Optional<BedType> foundBedType = bedTypeRepository.findByBedTypeName("King");

        assertTrue(foundBedType.isPresent());
        assertEquals("King", foundBedType.get().getTranslations().get(0).getBedTypeName());
    }

    @Test
    void testFindByBedTypeAndBedSize() {
        Optional<BedType> foundBedType = bedTypeRepository.findByBedTypeAndBedSize("King", "Large");

        assertTrue(foundBedType.isPresent());
        assertEquals("King", foundBedType.get().getTranslations().get(0).getBedTypeName());
        assertEquals("Large", foundBedType.get().getTranslations().get(0).getBedSize());
    }

    @Test
    void testFindByBedTypeAndBedSizeAndLanguage() {

        Optional<BedType> foundBedTypeEn = bedTypeRepository.findByBedTypeAndBedSizeAndLanguage("King", "Large", "en");

        assertTrue(foundBedTypeEn.isPresent());
        assertEquals("en", foundBedTypeEn.get().getTranslations().get(0).getLanguage());
        assertEquals("King", foundBedTypeEn.get().getTranslations().get(0).getBedTypeName());
        assertEquals("Large", foundBedTypeEn.get().getTranslations().get(0).getBedSize());

        Optional<BedType> foundBedTypeFr = bedTypeRepository.findByBedTypeAndBedSizeAndLanguage("King", "Large", "fr");

        assertTrue(foundBedTypeFr.isPresent());
        assertEquals("fr", foundBedTypeFr.get().getTranslations().get(1).getLanguage());
        assertEquals("King", foundBedTypeFr.get().getTranslations().get(1).getBedTypeName());
        assertEquals("Large", foundBedTypeFr.get().getTranslations().get(1).getBedSize());
    }

    @Test
    void testFindByBedTypeNameNotFound() {

        Optional<BedType> foundBedType = bedTypeRepository.findByBedTypeName("Single");

        assertFalse(foundBedType.isPresent());
    }

    @Test
    void testFindByBedTypeAndBedSizeNotFound() {

        Optional<BedType> foundBedType = bedTypeRepository.findByBedTypeAndBedSize("King", "Small");

        assertFalse(foundBedType.isPresent());
    }

    @Test
    void testFindByBedTypeAndBedSizeAndLanguageNotFound() {

        Optional<BedType> foundBedType = bedTypeRepository.findByBedTypeAndBedSizeAndLanguage("King", "Small", "es");

        assertFalse(foundBedType.isPresent());
    }
}
