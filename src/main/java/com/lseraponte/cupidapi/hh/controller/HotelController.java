package com.lseraponte.cupidapi.hh.controller;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
