package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.service.CupidApiService;
import com.lseraponte.cupidapi.hh.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CupidApiController.class)
class CupidApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CupidApiService cupidApiService;

    private HotelDTO hotelDTO;
    private List<ReviewDTO> reviewDTOList;
    private String language;

    @BeforeEach
    void setUp() throws IOException {
        hotelDTO = TestUtil.readJsonFromFile("src/test/resources/hotel.json", HotelDTO.class);
        reviewDTOList = TestUtil.readJsonListFromFile("src/test/resources/reviews.json", ReviewDTO.class);
        language = "en";
    }

    @Test
    void testGetHotel() throws Exception {
        int hotelId = hotelDTO.hotelId();

        when(cupidApiService.getHotelById(hotelId)).thenReturn(hotelDTO);

        mockMvc.perform(get("/cupid-api/hotels/{id}", hotelId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotel_id").value(1738870));


        verify(cupidApiService, times(1)).getHotelById(hotelId);
    }

    @Test
    void testGetHotelWithTranslation() throws Exception {
        int hotelId = hotelDTO.hotelId();

        when(cupidApiService.getHotelByIdWithTranslation(hotelId, language)).thenReturn(hotelDTO);

        mockMvc.perform(get("/cupid-api/hotels/{id}/lang/{language}", hotelId, language)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotel_id").value(1738870));

        verify(cupidApiService, times(1)).getHotelByIdWithTranslation(hotelId, language);
    }

    @Test
    void testGetReviews() throws Exception {
        int hotelId = hotelDTO.hotelId();
        int reviewsLimit = 5;

        when(cupidApiService.getHotelReviews(hotelId, reviewsLimit)).thenReturn(List.of(reviewDTOList.get(0)));

        mockMvc.perform(get("/cupid-api/hotels/reviews/{id}/{reviewsLimit}", hotelId, reviewsLimit)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(reviewDTOList.get(0).name()));

        verify(cupidApiService, times(1)).getHotelReviews(hotelId, reviewsLimit);
    }
}
