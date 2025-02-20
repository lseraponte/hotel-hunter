package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public Hotel saveHotelWithTranslation(Hotel hotel, String language) {

        return hotelRepository.save(hotel); // Saves hotel + translations in one go

    }

    public Optional<Hotel> getHotelByIdWithTranslation(int hotelId, String language) {
        return hotelRepository.findByHotelId(hotelId);
    }
}