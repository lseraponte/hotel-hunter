package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.service.CupidApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Cupid API", description = "Cupid API Endpoints")
@RestController
@RequestMapping("/cupid-api")
@RequiredArgsConstructor
public class CupidApiController {

    private final CupidApiService cupidApiService;

    @Operation(summary = "Get Hotel by ID", description = "Fetches hotel details by ID.")
    @GetMapping("/hotels/{id}")
    public Mono<HotelDTO> getHotel(@PathVariable int id) {
        return cupidApiService.getHotelById(id);
    }

    @Operation(summary = "Get Hotel with Translation", description = "Fetches hotel details with specified language.")
    @GetMapping("/hotels/{id}/lang/{language}")
    public Mono<HotelDTO> getHotelWithTranslation(@PathVariable int id, @PathVariable String language) {
        return cupidApiService.getHotelByIdWithTranslation(id, language);
    }

    @Operation(summary = "Get Hotel Reviews", description = "Fetches hotel reviews with a limit.")
    @GetMapping("/hotels/reviews/{id}/{reviewsLimit}")
    public Flux<ReviewDTO> getReviews(@PathVariable int id, @PathVariable() int reviewsLimit) {
        return cupidApiService.getHotelReviews(id, reviewsLimit);
    }

}
