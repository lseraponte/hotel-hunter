package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<Hotel> saveHotel(@RequestBody HotelDTO hotelDTO, @RequestParam String language) {

        Hotel hotel = Hotel.fromDTO(hotelDTO, language);
        Hotel savedHotel = hotelService.saveHotelWithTranslation(hotel, language);

        return ResponseEntity.ok(savedHotel);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotel(@PathVariable int hotelId, @RequestParam String language) {
        return hotelService.getHotelByIdWithTranslation(hotelId, language)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reviews")
    public ResponseEntity<List<Review>> addingHotelReviews(@RequestBody List<ReviewDTO> reviewDTOList, @RequestParam Integer hotelId) {

        List<Review> savedReviews = hotelService.addHotelReviews(hotelId, reviewDTOList);
        if (Objects.nonNull(savedReviews))
            return ResponseEntity.ok(savedReviews);

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/reviews/{hotelId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable int hotelId, @RequestParam(required = false) String language) {
        List<Review> hotelReviews = hotelService.getHotelReviews(hotelId, language);

        if (hotelReviews.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        return ResponseEntity.ok(hotelReviews);
    }

}
