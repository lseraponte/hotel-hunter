package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.Facility;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Room;
import com.lseraponte.cupidapi.hh.repository.AmenityRepository;
import com.lseraponte.cupidapi.hh.repository.FacilityRepository;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final FacilityRepository facilityRepository;
    private final AmenityRepository amenityRepository;

    public Hotel saveHotelWithTranslation(Hotel hotel, String language) {

        Optional<Hotel> savedHotel = hotelRepository.findByIdWithTranslationsByLanguage(hotel.getHotelId(), language);
        if (savedHotel.isPresent())
            return savedHotel.get();

        if (!"en".equals(language)) {
            savedHotel = hotelRepository.findByIdWithTranslationsByLanguage(hotel.getHotelId(), "en");
            if (savedHotel.isPresent()) {
                Hotel retrievedHotel = savedHotel.get();
                retrievedHotel.getTranslations().add(hotel.getTranslations().get(0));
                if (Objects.nonNull(hotel.getRooms())) {
                    for (int i = 0; i < retrievedHotel.getRooms().size(); i++) {
                        retrievedHotel.getRooms().get(i).getTranslations().add(hotel.getRooms().get(i).getTranslations().get(0));
                        List<Amenity> updatedAmenities = new ArrayList<>();
                        for (Amenity amenity : retrievedHotel.getRooms().get(i).getRoomAmenities()) {
                            Optional<Amenity> savedAmenity = amenityRepository.findByAmenityId(amenity.getAmenityId());
                            if (savedAmenity.isPresent()) {
                                Amenity existingAmenity = savedAmenity.get();
                                if (existingAmenity.getTranslations().stream().noneMatch(
                                        t -> language.equalsIgnoreCase(t.getLanguage())
                                )) {
                                    existingAmenity.getTranslations().add(amenity.getTranslations().get(0));
                                    updatedAmenities.add(savedAmenity.get());
                                }
                            } else {
                                updatedAmenities.add(amenityRepository.save(amenity));
                            }
                        }
                        retrievedHotel.getRooms().get(i).setRoomAmenities(updatedAmenities);

                    }
                }
                if (Objects.nonNull(hotel.getPolicies()) && !hotel.getPolicies().isEmpty()) {
                    for (int i = 0; i < retrievedHotel.getPolicies().size(); i++) {
                        retrievedHotel.getPolicies().get(i).getTranslations().add(hotel.getPolicies().get(i).getTranslations().get(0));
                    }
                }
                if (Objects.nonNull(hotel.getFacilities()) && !hotel.getFacilities().isEmpty()) {
                    for (int i = 0; i < retrievedHotel.getFacilities().size(); i++) {
                        retrievedHotel.getFacilities().get(i).getTranslations().add(hotel.getFacilities().get(i).getTranslations().get(0));
                    }
                }
                hotel = retrievedHotel;
            }
        }


        for (Room room : hotel.getRooms()) {
            List<Amenity> updatedAmenities = new ArrayList<>();
            for (Amenity amenity : room.getRoomAmenities()) {
                Optional<Amenity> savedAmenity = amenityRepository.findByAmenityId(amenity.getAmenityId());
                if (savedAmenity.isPresent()) {
                    updatedAmenities.add(savedAmenity.get());
                } else {
                    updatedAmenities.add(amenityRepository.save(amenity));
                }
            }
            room.setRoomAmenities(updatedAmenities);
        }

        List<Facility> updatedFacilities = new ArrayList<>();
        for (Facility facility : hotel.getFacilities()) {
            Optional<Facility> savedFacility = facilityRepository.findByFacilityId(facility.getFacilityId());
            if (savedFacility.isPresent()) {
                updatedFacilities.add(savedFacility.get());
            } else {
                updatedFacilities.add(facilityRepository.save(facility));
            }
        }
        hotel.setFacilities(updatedFacilities);

        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> getHotelByIdWithTranslation(int hotelId, String language) {

        Optional<Hotel> savedHotel = hotelRepository.findByIdWithTranslationsByLanguage(hotelId, language);

        if (savedHotel.isEmpty() && !"EN".equals(language)) {
            savedHotel = hotelRepository.findByIdWithTranslationsByLanguage(hotelId, "EN");
        }

        return hotelRepository.findByHotelId(hotelId);

    }
}