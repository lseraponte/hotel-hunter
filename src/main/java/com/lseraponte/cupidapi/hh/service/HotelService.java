package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.Facility;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Policy;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
                        for (Amenity amenity : hotel.getRooms().get(i).getRoomAmenities()) {
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

                    Map<Integer, Policy> policyMap = hotel.getPolicies().stream()
                            .collect(Collectors.toMap(Policy::getPolicyId, p -> p));

                    for (Policy retrievedPolicy : retrievedHotel.getPolicies()) {
                        Policy matchingPolicy = policyMap.get(retrievedPolicy.getPolicyId());

                        if (matchingPolicy != null && !matchingPolicy.getTranslations().isEmpty()) {
                            retrievedPolicy.getTranslations().add(matchingPolicy.getTranslations().get(0));
                        }
                    }

                    // If there are additional policies we'll be adding them at the end
                    for (Policy newPolicy : hotel.getPolicies()) {
                        if (retrievedHotel.getPolicies().stream()
                                .noneMatch(p -> p.getPolicyId().equals(newPolicy.getPolicyId()))) {
                            retrievedHotel.getPolicies().add(newPolicy);
                        }
                    }
                }
                if (Objects.nonNull(hotel.getFacilities()) && !hotel.getFacilities().isEmpty()) {

                    Map<Integer, Facility> facilityMap = hotel.getFacilities().stream()
                            .collect(Collectors.toMap(Facility::getFacilityId, f -> f));

                    for (Facility retrievedFacility : retrievedHotel.getFacilities()) {
                        Facility matchingFacility = facilityMap.get(retrievedFacility.getFacilityId());

                        if (matchingFacility != null && !matchingFacility.getTranslations().isEmpty()) {
                            retrievedFacility.getTranslations().add(matchingFacility.getTranslations().get(0));
                        }
                    }

                    // If there are additional facilities we'll be adding them at the end
                    for (Facility newFacility : hotel.getFacilities()) {
                        if (retrievedHotel.getFacilities().stream()
                                .noneMatch(f -> f.getFacilityId().equals(newFacility.getFacilityId()))) {
                            retrievedHotel.getFacilities().add(newFacility);
                        }
                    }
                }
                return hotelRepository.save(retrievedHotel);
            }
        }


        for (Room room : hotel.getRooms()) {
            List<Amenity> updatedAmenities = new ArrayList<>();

            for (Amenity amenity : room.getRoomAmenities()) {
                Amenity existingAmenity = amenityRepository.findByAmenityId(amenity.getAmenityId())
                        .orElseGet(() -> amenityRepository.save(amenity)); // Save only if not found

                updatedAmenities.add(existingAmenity);
            }

            room.setRoomAmenities(updatedAmenities);
        }

        List<Facility> updatedFacilities = new ArrayList<>();
        for (Facility facility : hotel.getFacilities()) {
            Facility existingFacility = facilityRepository.findByFacilityId(facility.getFacilityId())
                    .orElseGet(() -> facilityRepository.save(facility));

            updatedFacilities.add(existingFacility);

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