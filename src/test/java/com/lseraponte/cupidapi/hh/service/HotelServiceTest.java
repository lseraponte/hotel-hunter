package com.lseraponte.cupidapi.hh.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import com.lseraponte.cupidapi.hh.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.util.*;

class HotelServiceTest {

    @Mock private HotelRepository hotelRepository;

    @InjectMocks private HotelService hotelService;

    private HotelDTO hotelDTO;
    private List<ReviewDTO> reviewDTOList;
    private Hotel hotel;
    private String language;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        hotelDTO = TestUtil.readJsonFromFile("src/test/resources/hotel.json", HotelDTO.class);
        reviewDTOList = TestUtil.readJsonListFromFile("src/test/resources/reviews.json", ReviewDTO.class);
        language = "en";
        hotel = Hotel.fromDTO(hotelDTO, language);

        when(hotelRepository.findById(anyInt())).thenReturn(Optional.of(hotel));
    }

    @Test
    void testSaveHotelWithTranslation_success() {
        when(hotelRepository.findByIdWithTranslationsByLanguage(anyInt(), anyString()))
                .thenReturn(Optional.empty());
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        Hotel savedHotel = hotelService.saveHotelWithTranslation(hotelDTO, "EN", reviewDTOList);

        assertNotNull(savedHotel);
        assertEquals(0, savedHotel.getStars());
    }



    @Test
    void testSaveHotelWithTranslation_existingHotel() {
        when(hotelRepository.findByIdWithTranslationsByLanguage(anyInt(), anyString()))
                .thenReturn(Optional.of(hotel));

        Hotel savedHotel = hotelService.saveHotelWithTranslation(hotelDTO, "EN", reviewDTOList);

        assertNotNull(savedHotel);
        assertEquals(0, savedHotel.getStars());
    }

    @Test
    void testUpdateHotel_hotelNotFound() {
        when(hotelRepository.findById(anyInt())).thenReturn(Optional.empty());

        Hotel updatedHotel = hotelService.updateHotel(hotelDTO, reviewDTOList, "EN");

        assertNull(updatedHotel);
    }

    @Test
    void testGetHotelByIdWithTranslations_success() {
        when(hotelRepository.findByIdWithTranslationsByLanguage(anyInt(), anyString()))
                .thenReturn(Optional.of(hotel));

        Optional<Hotel> retrievedHotel = hotelService.getHotelByIdWithTranslations(1, "EN");

        assertTrue(retrievedHotel.isPresent());
        assertEquals(0, retrievedHotel.get().getStars());
    }

    @Test
    void testGetHotelByIdWithTranslations_notFound() {
        when(hotelRepository.findByIdWithTranslationsByLanguage(anyInt(), anyString()))
                .thenReturn(Optional.empty());

        Optional<Hotel> retrievedHotel = hotelService.getHotelByIdWithTranslations(1, "EN");

        assertFalse(retrievedHotel.isPresent());
    }

    @Test
    void testDeleteHotelById_success() {
        when(hotelRepository.existsById(anyInt())).thenReturn(true);

        hotelService.deleteHotelById(1);

        verify(hotelRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testAddHotelReviews_success() {
        List<Review> reviews = hotelService.addHotelReviews(1, reviewDTOList);

        assertNotNull(reviews);
        assertEquals(50, reviews.size());
    }

    @Test
    void testAddHotelReviews_hotelNotFound() {
        when(hotelRepository.findById(anyInt())).thenReturn(Optional.empty());

        List<Review> reviews = hotelService.addHotelReviews(1, reviewDTOList);

        assertNull(reviews);
    }

    @Test
    void testGetHotelReviews_success() {
        List<Review> mockedReviews = reviewDTOList.stream().map(Review::fromDTO).toList();
        when(hotelRepository.findReviewsByHotelIdAndLanguage(anyInt(), anyString()))
                .thenReturn(mockedReviews);

        List<Review> reviews = hotelService.getHotelReviews(1, "EN");

        assertNotNull(reviews);
        assertEquals(50, reviews.size());
    }
}
