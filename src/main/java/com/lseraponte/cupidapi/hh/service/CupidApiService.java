package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CupidApiService {

    private static final Logger logger = LoggerFactory.getLogger(CupidApiService.class);
    private final RestTemplate restTemplate;
    private final String baseUrl = "https://content-api.cupid.travel/v3.0";
    HttpEntity<String> entity = new HttpEntity<>(createHeaders());

    public HotelDTO getHotelById(int hotelId) {
        try {
            ResponseEntity<String> rawResponse = restTemplate.exchange(baseUrl + "/property/" + hotelId, HttpMethod.GET, entity, String.class);
            logger.info("Raw API Response: {}", rawResponse.getBody());

            ResponseEntity<HotelDTO> response = restTemplate.exchange(baseUrl + "/property/" + hotelId, HttpMethod.GET, entity, HotelDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("API Error: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("Service Error: " + e.getMessage(), e);
        }
    }

    public HotelDTO getHotelByIdWithTranslation(int hotelId, String language) {
        try {
            ResponseEntity<HotelDTO> response = restTemplate.exchange(baseUrl + "/property/" + hotelId + "/lang/" + language, HttpMethod.GET, entity, HotelDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("API Error: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("Service Error: " + e.getMessage(), e);
        }
    }

    public List<ReviewDTO> getHotelReviews(int hotelId, int reviewLimit) {
        try {
            ResponseEntity<ReviewDTO[]> response = restTemplate.exchange(baseUrl + "/property/reviews/" + hotelId + "/" + reviewLimit, HttpMethod.GET, entity, ReviewDTO[].class);
            return Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("API Error: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("Service Error: " + e.getMessage(), e);
        }
    }

    public HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "i2O4p6A8s0D3f5G7h9J1k3L5m7N9b");
        return headers;
    }
}