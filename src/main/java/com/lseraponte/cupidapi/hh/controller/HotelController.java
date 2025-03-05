package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.HotelWithTranslationDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Tag(name = "Hotel API", description = "Hotel Management Endpoints")
@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Save Hotel", description = "Saves a new hotel with translation and optional reviews")
    @PostMapping
    public ResponseEntity<Hotel> saveHotel(@RequestBody HotelDTO hotelDTO,
                                           @RequestBody(required = false) List<ReviewDTO> reviewDTOList,
                                           @RequestParam String language) {

        Hotel savedHotel = hotelService.saveHotelWithTranslation(hotelDTO, language, reviewDTOList);
        return ResponseEntity.ok(savedHotel);
    }

    @Operation(summary = "Get Hotel by ID", description = "Retrieves a hotel by its identifier with optional translations")
    @GetMapping("/search/identifier/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable int hotelId,
                                          @RequestParam(required = false) String language) {
        return hotelService.getHotelByIdWithTranslations(hotelId, language)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get Hotel by Name", description = "Retrieves a hotel by its name with optional translations")
    @GetMapping("/search/name/{hotelName}")
    public ResponseEntity<Hotel> getHotelByName(@PathVariable String hotelName,
                                          @RequestParam(required = false) String language) {
        return hotelService.getHotelByNameWithTranslationsByLanguage(hotelName, language)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get Hotels by City", description = "Retrieves hotels by city with optional translations")
    @GetMapping("/search/location/{city}")
    public ResponseEntity<List<HotelWithTranslationDTO>> getHotelByCity(@PathVariable String city,
                                                                        @RequestParam(required = false) String language) {
        return hotelService.getHotelsByCityWithTranslationsByLanguage(city, language)
                .filter(list -> !list.isEmpty())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update Hotel", description = "Updates hotel details by ID with optional reviews")
    @PutMapping("/update")
    public ResponseEntity<Hotel> updateHotelById(@RequestBody HotelDTO hotelDTO,
                                                 @RequestBody(required = false) List<ReviewDTO> reviewDTOList,
                                                 @RequestParam(required = false) String language) {
        Hotel updatedHotel = hotelService.updateHotel(hotelDTO, reviewDTOList, language);
        return ResponseEntity.ok(updatedHotel);
    }

    @Operation(summary = "Delete Hotel", description = "Deletes a hotel by its ID")
    @DeleteMapping("/delete/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Integer hotelId) {
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add Hotel Reviews", description = "Adds reviews for a specific hotel")
    @PostMapping("/reviews")
    public ResponseEntity<List<Review>> addingHotelReviews(@RequestBody List<ReviewDTO> reviewDTOList,
                                                           @RequestParam Integer hotelId) {
        List<Review> savedReviews = hotelService.addHotelReviews(hotelId, reviewDTOList);
        if (Objects.nonNull(savedReviews))
            return ResponseEntity.ok(savedReviews);

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get Hotel Reviews", description = "Retrieves hotel reviews with optional language translation")
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
