package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PhotoRepositoryTest {

    @Autowired
    private PhotoRepository photoRepository;

    private Photo photo;

    @BeforeEach
    void setUp() {
        // Prepare the test data
        photo = Photo.builder()
                .url("https://example.com/photo.jpg")
                .hdUrl("https://example.com/photo_hd.jpg")
                .imageDescription("A beautiful view")
                .imageClass1("landscape")
                .imageClass2("nature")
                .mainPhoto(true)
                .score(4.5)
                .classId(1)
                .classOrder(1)
                .build();

        photoRepository.save(photo);  // Save photo to the database
    }

    @Test
    void testFindByUrl() {

        Optional<Photo> foundPhoto = photoRepository.findByUrl(photo.getUrl());

        assertTrue(foundPhoto.isPresent());
        assertEquals(photo.getUrl(), foundPhoto.get().getUrl());
        assertEquals(photo.getImageDescription(), foundPhoto.get().getImageDescription());
    }

    @Test
    void testFindByUrlNotFound() {

        Optional<Photo> foundPhoto = photoRepository.findByUrl("https://example.com/nonexistent.jpg");

        assertFalse(foundPhoto.isPresent());
    }
}
