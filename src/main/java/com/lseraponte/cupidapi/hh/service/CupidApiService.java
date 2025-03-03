package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class CupidApiService {

    private final WebClient webClient;

    public CupidApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<HotelDTO> getHotelById(int hotelId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/property/{id}").build(hotelId))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)))
                )
                .bodyToMono(HotelDTO.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5))
                        .filter(throwable -> throwable instanceof RuntimeException));

    }

    public Mono<HotelDTO> getHotelByIdWithTranslation(int hotelId, String language) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/property/{id}/lang/{language}").build(hotelId, language))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)))
                )
                .bodyToMono(HotelDTO.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5))
                        .filter(throwable -> throwable instanceof RuntimeException));
    }

    public Flux<ReviewDTO> getHotelReviews(int hotelId, int reviewLimit) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/property/reviews/{id}/{reviewLimit}")
                        .build(hotelId, reviewLimit))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("API Error: " + errorBody)))
                )
                .bodyToFlux(ReviewDTO.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5))
                        .filter(throwable -> throwable instanceof RuntimeException));
    }

}
