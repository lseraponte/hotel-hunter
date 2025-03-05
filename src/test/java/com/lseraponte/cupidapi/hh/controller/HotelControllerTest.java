package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.service.HotelService;
import com.lseraponte.cupidapi.hh.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

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
    void getHotelById_ShouldReturnHotel() throws Exception {
        Hotel hotel = Hotel.fromDTO(hotelDTO, language);
        when(hotelService.getHotelByIdWithTranslations(anyInt(), any())).thenReturn(Optional.of(hotel));

        mockMvc.perform(get("/hotels/search/identifier/1")
                        .param("language", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hotelId").value(1738870));
    }

    @Test
    void getHotelById_ShouldReturnNotFound() throws Exception {
        when(hotelService.getHotelByIdWithTranslations(anyInt(), any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/hotels/search/identifier/99")
                        .param("language", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteHotelById_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(hotelService).deleteHotelById(anyInt());

        mockMvc.perform(delete("/hotels/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getReviews_ShouldReturnReviews() throws Exception {
        Review review = Review.fromDTO(reviewDTOList.get(0));
        when(hotelService.getHotelReviews(anyInt(), any())).thenReturn(List.of(review));

        mockMvc.perform(get("/hotels/reviews/1")
                        .param("language", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].averageScore").value(10))
                .andExpect(jsonPath("$[0].language").value("en"));
    }

    @Test
    void getReviews_ShouldReturnNotFound() throws Exception {
        when(hotelService.getHotelReviews(anyInt(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/hotels/reviews/1")
                        .param("language", "en")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
