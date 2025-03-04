package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.service.CupidApiService;
import com.lseraponte.cupidapi.hh.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToController;
import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(CupidApiController.class)
public class CupidApiControllerTest {

    @MockBean
    private CupidApiService cupidApiService;

    private WebTestClient webTestClient;
    private CupidApiController cupidApiController;

    private HotelDTO hotelDTO;
    private List<ReviewDTO> reviewDTOList;
    private String language;

    @BeforeEach
    void setUp() throws IOException {

        cupidApiController = new CupidApiController(cupidApiService);

        webTestClient = bindToController(cupidApiController).build();

        hotelDTO = TestUtil.readJsonFromFile("src/test/resources/hotel.json", HotelDTO.class);
        reviewDTOList = TestUtil.readJsonListFromFile("src/test/resources/reviews.json", ReviewDTO.class);
        language = "en";
    }

    @Test
    void testGetHotel() {
        int hotelId = hotelDTO.hotelId();

        when(cupidApiService.getHotelById(hotelId)).thenReturn(Mono.just(hotelDTO));

        webTestClient.get()
                .uri("/cupid-api/hotels/{id}", hotelId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HotelDTO.class)
                .value(hotel -> assertThat(hotel).isEqualTo(hotelDTO));

        verify(cupidApiService, times(1)).getHotelById(hotelId);
    }

    @Test
    void testGetHotelWithTranslation() {
        int hotelId = hotelDTO.hotelId();

        when(cupidApiService.getHotelByIdWithTranslation(hotelId, language)).thenReturn(Mono.just(hotelDTO));

        webTestClient.get()
                .uri("/cupid-api/hotels/{id}/lang/{language}", hotelId, language)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HotelDTO.class)
                .value(hotel -> assertThat(hotel).isEqualTo(hotelDTO));

        verify(cupidApiService, times(1)).getHotelByIdWithTranslation(hotelId, language);
    }

    @Test
    void testGetReviews() {
        int hotelId = hotelDTO.hotelId();
        int reviewsLimit = 5;

        when(cupidApiService.getHotelReviews(hotelId, reviewsLimit)).thenReturn(Flux.just(reviewDTOList.get(0)));

        webTestClient.get()
                .uri("/cupid-api/hotels/reviews/{id}/{reviewsLimit}", hotelId, reviewsLimit)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReviewDTO.class)
                .value(reviews -> assertThat(reviews).hasSize(1).contains(reviewDTOList.get(0)));

        verify(cupidApiService, times(1)).getHotelReviews(hotelId, reviewsLimit);
    }
}
