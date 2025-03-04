package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.AddressDTO;
import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.HotelWithTranslationDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.service.HotelService;
import com.lseraponte.cupidapi.hh.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebFluxTest(HotelController.class)
public class HotelControllerTest {

    @MockBean
    private HotelService hotelService;

    private WebTestClient webTestClient;
    private HotelController hotelController;

    private HotelDTO hotelDTO;
    private List<ReviewDTO> reviewDTOList;
    private String language;
    private String city;

    @BeforeEach
    void setUp() throws IOException {

        hotelController = new HotelController(hotelService);

        webTestClient = WebTestClient.bindToController(hotelController).build();

        hotelDTO = TestUtil.readJsonFromFile("src/test/resources/hotel.json", HotelDTO.class);
        reviewDTOList = TestUtil.readJsonListFromFile("src/test/resources/reviews.json", ReviewDTO.class);
        language = "en";
        city = "New York";
    }

    @Test
    void testGetHotelById() {
        int hotelId = hotelDTO.hotelId();

        when(hotelService.getHotelByIdWithTranslations(hotelId, language)).thenReturn(Optional.of(new Hotel()));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hotels/search/identifier/{hotelId}")
                        .queryParam("language", language)
                        .build(hotelId))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Hotel.class);

        verify(hotelService, times(1)).getHotelByIdWithTranslations(hotelId, language);
    }

    @Test
    void testGetHotelByName() {
        String hotelName = hotelDTO.hotelName();

        when(hotelService.getHotelByNameWithTranslationsByLanguage(hotelName, language))
                .thenReturn(Optional.of(new Hotel()));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hotels/search/name/{hotelName}")
                        .queryParam("language", language)
                        .build(hotelName))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Hotel.class);

        verify(hotelService, times(1)).getHotelByNameWithTranslationsByLanguage(hotelName, language);
    }

    @Test
    void testGetHotelsByCity() {
        String city = "SampleCity";
        List<HotelWithTranslationDTO> hotelWithTranslationDTOList = List.of(new HotelWithTranslationDTO(
                1,             // hotelId
                1234,                 // cupidId
                "Hotel Paradise",     // hotelName
                5,                    // stars
                "A luxurious hotel in the heart of the city.",  // description
                "## Welcome to Hotel Paradise", // markdownDescription
                "No pets allowed. Please check-in between 2PM and 10PM.", // importantInfo
                new AddressDTO("123 Main St", "Cityville", "Country"), // address
                "+123456789",         // phone
                "+123456780",         // fax
                "info@hotelparadise.com", // email
                4.5,                  // rating
                150,                  // reviewCount
                "14:00",              // checkinStart
                "22:00",              // checkinEnd
                "11:00",              // checkout
                "Free parking available", // parking
                true,                 // childAllowed
                false,                // petsAllowed
                "https://example.com/images/main_image.jpg"
        ));

        when(hotelService.getHotelsByCityWithTranslationsByLanguage(city, language))
                .thenReturn(Optional.of(hotelWithTranslationDTOList));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hotels/search/location/{city}")
                        .queryParam("language", language)
                        .build(city))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(HotelWithTranslationDTO.class)
                .value(hotels -> assertThat(hotels).hasSize(1).containsExactlyElementsOf(hotelWithTranslationDTOList));

        verify(hotelService, times(1)).getHotelsByCityWithTranslationsByLanguage(city, language);
    }

    @Test
    void testDeleteHotelById() {
        int hotelId = hotelDTO.hotelId();

        doNothing().when(hotelService).deleteHotelById(hotelId);

        webTestClient.delete()
                .uri("/hotels/delete/{hotelId}", hotelId)
                .exchange()
                .expectStatus().isNoContent();

        verify(hotelService, times(1)).deleteHotelById(hotelId);
    }

    @Test
    void testAddHotelReviews() {
        int hotelId = hotelDTO.hotelId();

        when(hotelService.addHotelReviews(hotelId, reviewDTOList)).thenReturn(new ArrayList<>());

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/hotels/reviews")
                        .queryParam("hotelId", hotelId)
                        .build())
                .bodyValue(reviewDTOList)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Review.class);

        verify(hotelService, times(1)).addHotelReviews(hotelId, reviewDTOList);
    }


    @Test
    void testGetReviews() {
        int hotelId = hotelDTO.hotelId();

        when(hotelService.getHotelReviews(hotelId, language)).thenReturn(List.of(new Review()));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/hotels/reviews/{hotelId}")
                        .queryParam("language", language)
                        .build(hotelId))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Review.class);

        verify(hotelService, times(1)).getHotelReviews(hotelId, language);
    }

}
