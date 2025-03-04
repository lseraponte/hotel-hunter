package com.lseraponte.cupidapi.hh.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.lseraponte.cupidapi.hh.model.*;
import com.lseraponte.cupidapi.hh.dto.HotelWithTranslationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel hotel;
    private HotelTranslation hotelTranslation;

    @BeforeEach
    void setUp() {
        hotelTranslation = HotelTranslation.builder()
                .hotelType("Luxury")
                .hotelName("Hotel Test")
                .chain("Test Chain")
                .description("Test Description")
                .markdownDescription("Markdown Description")
                .importantInfo("Important Info")
                .language("en")
                .build();

        hotel = Hotel.builder()
                .hotelId(1)
                .cupidId(100)
                .mainImageTh("main_image")
                .stars(5)
                .address(new Address("Test Address", "Test City", "Test Country"))
                .translations(List.of(hotelTranslation))
                .build();

        hotelRepository.save(hotel);
    }

    @Test
    void testFindAllHotelIds() {
        List<Integer> hotelIds = hotelRepository.findAllHotelIds();

        assertThat(hotelIds).isNotEmpty();
        assertThat(hotelIds.get(0)).isEqualTo(hotel.getHotelId());
    }

    @Test
    void testFindByIdWithTranslationsByLanguage() {
        Optional<Hotel> foundHotel = hotelRepository.findByIdWithTranslationsByLanguage(hotel.getHotelId(), "en");

        assertThat(foundHotel).isPresent();
        assertThat(foundHotel.get().getHotelId()).isEqualTo(hotel.getHotelId());
        assertThat(foundHotel.get().getTranslations()).isNotEmpty();
    }

    @Test
    void testFindByNameWithTranslationsByLanguage() {
        Optional<Hotel> foundHotel = hotelRepository.findByNameWithTranslationsByLanguage("Hotel Test", "en");

        assertThat(foundHotel).isPresent();
        assertThat(foundHotel.get().getHotelId()).isEqualTo(hotel.getHotelId());
        assertThat(foundHotel.get().getTranslations()).isNotEmpty();
    }

    @Test
    void testFindHotelsWithTranslationsByCityAndLanguage() {
        Optional<List<HotelWithTranslationDTO>> hotels = hotelRepository.findHotelsWithTranslationsByCityAndLanguage("Test City", "en");

        assertThat(hotels).isPresent();
        assertThat(hotels.get()).hasSize(1);
        assertThat(hotels.get().get(0).hotelId()).isEqualTo(hotel.getHotelId());
    }

    @Test
    void testFindReviewsByHotelId() {
        List<Review> reviews = hotelRepository.findReviewsByHotelId(hotel.getHotelId());

        assertThat(reviews).isEmpty();
    }

    @Test
    void testFindFacilitiesByHotelId() {
        List<Facility> facilities = hotelRepository.findFacilitiesByHotelId(hotel.getHotelId());

        assertThat(facilities).isEmpty();
    }

    @Test
    void testFindPhotosByHotelId() {
        List<Photo> photos = hotelRepository.findPhotosByHotelId(hotel.getHotelId());

        assertThat(photos).isEmpty();
    }

    @Test
    void testFindPolicyByHotelId() {
        List<Policy> policies = hotelRepository.findPolicyByHotelId(hotel.getHotelId());

        assertThat(policies).isEmpty();
    }

    @Test
    void testFindRoomByHotelId() {
        List<Room> rooms = hotelRepository.findRoomByHotelId(hotel.getHotelId());

        assertThat(rooms).isEmpty();
    }

    @Test
    void testFindReviewsByHotelIdAndLanguage() {
        List<Review> reviews = hotelRepository.findReviewsByHotelIdAndLanguage(hotel.getHotelId(), "en");

        assertThat(reviews).isEmpty();
    }

    @Test
    void testDeleteById() {
        hotelRepository.deleteById(hotel.getHotelId());
        Optional<Hotel> deletedHotel = hotelRepository.findById(hotel.getHotelId());

        assertThat(deletedHotel).isNotPresent();
    }
}
