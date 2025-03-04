package com.lseraponte.cupidapi.hh.repository;

import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.BedType;
import com.lseraponte.cupidapi.hh.model.Photo;
import com.lseraponte.cupidapi.hh.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    private Room room;
    private Amenity amenity;
    private BedType bedType;
    private Photo photo;

    @BeforeEach
    void setUp() {

        amenity = Amenity.builder().amenityId(1).build();
        bedType = BedType.builder().quantity(1).build();
        photo = Photo.builder().url("photo-url").build();

        room = Room.builder()
                .id(1)
                .roomSizeSquare(30)
                .roomSizeUnit("m2")
                .maxAdults(2)
                .maxChildren(2)
                .maxOccupancy(4)
                .roomAmenities(List.of(amenity))
                .bedTypes(List.of(bedType))
                .photos(List.of(photo))
                .build();

        roomRepository.save(room);
    }

    @Test
    void testFindAmenityByRoomId() {

        List<Amenity> amenities = roomRepository.findAmenityByRoomId(room.getId());

        assertNotNull(amenities);
        assertEquals(1, amenities.size());
        assertEquals(amenity.getAmenityId(), amenities.get(0).getAmenityId());
    }

    @Test
    void testFindBedTypeByRoomId() {

        List<BedType> bedTypes = roomRepository.findBedTypeByRoomId(room.getId());

        assertNotNull(bedTypes);
        assertEquals(1, bedTypes.size());
        assertEquals(bedType.getQuantity(), bedTypes.get(0).getQuantity());
    }

    @Test
    void testFindPhotosByRoomId() {

        List<Photo> photos = roomRepository.findPhotosByRoomId(room.getId());

        assertNotNull(photos);
        assertEquals(1, photos.size());
        assertEquals(photo.getUrl(), photos.get(0).getUrl());
    }

    @Test
    void testFindAmenityByRoomIdNotFound() {

        List<Amenity> amenities = roomRepository.findAmenityByRoomId(999);

        assertTrue(amenities.isEmpty());
    }

    @Test
    void testFindBedTypeByRoomIdNotFound() {

        List<BedType> bedTypes = roomRepository.findBedTypeByRoomId(999);

        assertTrue(bedTypes.isEmpty());
    }

    @Test
    void testFindPhotosByRoomIdNotFound() {

        List<Photo> photos = roomRepository.findPhotosByRoomId(999);

        assertTrue(photos.isEmpty());
    }
}
