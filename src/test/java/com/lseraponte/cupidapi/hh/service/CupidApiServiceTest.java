package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

class CupidApiServiceTest {

    @InjectMocks
    private CupidApiService cupidApiService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<HotelDTO> hotelResponseEntity;

    @Mock
    private ResponseEntity<ReviewDTO[]> reviewResponseEntity;

    private HttpEntity<String> entity;
    private HotelDTO hotelDTO;
    private List<ReviewDTO> reviewDTOList;
    private String language;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        entity = new HttpEntity<>(cupidApiService.createHeaders());

        hotelDTO = TestUtil.readJsonFromFile("src/test/resources/hotel.json", HotelDTO.class);
        reviewDTOList = TestUtil.readJsonListFromFile("src/test/resources/reviews.json", ReviewDTO.class);
        language = "en";
    }

    @Test
    void testGetHotelByIdWithTranslation_Success() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(HotelDTO.class))).thenReturn(hotelResponseEntity);
        when(hotelResponseEntity.getBody()).thenReturn(hotelDTO);

        HotelDTO result = cupidApiService.getHotelByIdWithTranslation(123, "en");

        assertNotNull(result);
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(HotelDTO.class));
    }

    @Test
    void testGetHotelReviews_Success() {
        ReviewDTO[] reviewArray = reviewDTOList.toArray(new ReviewDTO[0]);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(ReviewDTO[].class))).thenReturn(reviewResponseEntity);
        when(reviewResponseEntity.getBody()).thenReturn(reviewArray);

        List<ReviewDTO> result = cupidApiService.getHotelReviews(123, 2);

        assertNotNull(result);
        assertEquals(50, result.size());
        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(ReviewDTO[].class));
    }

    @Test
    void testGetHotelById_ApiError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(String.class))).thenThrow(HttpClientErrorException.class);

        assertThrows(RuntimeException.class, () -> cupidApiService.getHotelById(123));
    }

    @Test
    void testGetHotelReviews_ServiceError() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(ReviewDTO[].class))).thenThrow(RestClientException.class);

        assertThrows(RuntimeException.class, () -> cupidApiService.getHotelReviews(123, 2));
    }
}
