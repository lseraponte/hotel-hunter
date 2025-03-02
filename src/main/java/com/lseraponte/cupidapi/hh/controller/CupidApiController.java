package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.service.CupidApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cupid-api")
@RequiredArgsConstructor
public class CupidApiController {

    private final CupidApiService cupidApiService;

    // Cupid APIs direct requests endpoints
    @GetMapping("/hotels/{id}")
    public Mono<HotelDTO> getHotel(@PathVariable int id) {
        return cupidApiService.getHotelById(id);
    }

    @GetMapping("/hotels/{id}/lang/{language}")
    public Mono<HotelDTO> getHotelWithTranslation(@PathVariable int id, @PathVariable String language) {
        return cupidApiService.getHotelByIdWithTranslation(id, language);
    }

    @GetMapping("/hotels/reviews/{id}/{reviewsLimit}")
    public Flux<ReviewDTO> getReviews(@PathVariable int id, @PathVariable() int reviewsLimit) {
        return cupidApiService.getHotelReviews(id, reviewsLimit);
    }

}
