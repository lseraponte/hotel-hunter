package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.service.CupidApiService;
import com.lseraponte.cupidapi.hh.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final CupidApiService cupidApiService;

    @GetMapping("/cupid-api/hotels/{id}")
    public Mono<HotelDTO> getHotel(@PathVariable int id) {
        return cupidApiService.getHotelById(id);
    }

    @GetMapping("/cupid-api/hotels/{id}/lang/{language}")
    public Mono<HotelDTO> getHotelWithTranslation(@PathVariable int id, @PathVariable String language) {
        return cupidApiService.getHotelByIdWithTranslation(id, language);
    }

    @GetMapping("/cupid-api/hotels/reviews/{id}/{reviewsLimit}")
    public Flux<ReviewDTO> getReviews(@PathVariable int id, @PathVariable() int reviewsLimit) {
        return cupidApiService.getHotelReviews(id, reviewsLimit);
    }




    @PostMapping
    public ResponseEntity<Hotel> saveHotel(@RequestBody HotelDTO hotelDTO,
                                           @RequestBody(required = false) List<ReviewDTO> reviewDTOList,
                                           @RequestParam String language) {

        Hotel savedHotel = hotelService.saveHotelWithTranslation(hotelDTO, language, reviewDTOList);
        return ResponseEntity.ok(savedHotel);
    }

    @GetMapping("/search/identifier/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable int hotelId,
                                          @RequestParam(required = false) String language) {
        return hotelService.getHotelByIdWithTranslations(hotelId, language)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/name/{hotelName}")
    public ResponseEntity<Hotel> getHotelByName(@PathVariable String hotelName,
                                          @RequestParam(required = false) String language) {
        return hotelService.getHotelByNameWithTranslationsByLanguage(hotelName, language)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reviews")
    public ResponseEntity<List<Review>> addingHotelReviews(@RequestBody List<ReviewDTO> reviewDTOList,
                                                           @RequestParam Integer hotelId) {

        List<Review> savedReviews = hotelService.addHotelReviews(hotelId, reviewDTOList);
        if (Objects.nonNull(savedReviews))
            return ResponseEntity.ok(savedReviews);

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/reviews/{hotelId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable int hotelId,
                                                   @RequestParam(required = false) String language) {
        List<Review> hotelReviews = hotelService.getHotelReviews(hotelId, language);

        if (hotelReviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        return ResponseEntity.ok(hotelReviews);
    }

}
