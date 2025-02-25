package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.Facility;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Policy;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.model.Room;
import com.lseraponte.cupidapi.hh.repository.AmenityRepository;
import com.lseraponte.cupidapi.hh.repository.FacilityRepository;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import com.lseraponte.cupidapi.hh.repository.PolicyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final FacilityRepository facilityRepository;
    private final AmenityRepository amenityRepository;
    private final PolicyRepository policyRepository;

    @Transactional
    public Hotel saveHotelWithTranslation(HotelDTO hotelDTO, String language, List<ReviewDTO> reviewDTOList) {

        Hotel hotel = Hotel.fromDTO(hotelDTO, language);
        boolean containsReviews = false;
        List<Review> reviewList = new ArrayList<>();;

        if (Objects.nonNull(reviewDTOList)) {
            containsReviews = true;
            for (ReviewDTO reviewDto : reviewDTOList) {
                reviewList.add(Review.fromDTO(reviewDto));
            }
        }

        Optional<Hotel> savedHotel = hotelRepository.findById(hotel.getHotelId());
        if (savedHotel.isPresent()) {
            Hotel retrievedHotel = savedHotel.get();
            boolean hasLanguage = retrievedHotel.getTranslations()
                    .stream()
                    .anyMatch(translation -> language.equalsIgnoreCase(translation.getLanguage()));
            if(hasLanguage) {
                if (containsReviews && retrievedHotel.getReviews() == null) {
                    retrievedHotel.setReviews(reviewList);
                    retrievedHotel = hotelRepository.save(retrievedHotel);
                }
                return retrievedHotel;
            } else {
                retrievedHotel.getTranslations().add(hotel.getTranslations().get(0));
                if (Objects.nonNull(hotel.getRooms())) {
                    retrievedHotel.getRooms().sort(Comparator.comparing(Room::getId));
                    hotel.getRooms().sort(Comparator.comparing(Room::getId));
                    for (int i = 0; i < retrievedHotel.getRooms().size(); i++) {
                        Room currentRoomSavedRoom = retrievedHotel.getRooms().get(i);
                        currentRoomSavedRoom.getTranslations().add(hotel.getRooms().get(i).getTranslations().get(0));
                        List<Amenity> updatedAmenities = new ArrayList<>();
                        for (Amenity amenity : hotel.getRooms().get(i).getRoomAmenities()) {
                            Optional<Amenity> savedAmenity = amenityRepository.findByAmenityId(amenity.getAmenityId());
                            if (savedAmenity.isPresent()) {
                                Amenity existingAmenity = savedAmenity.get();
                                if (existingAmenity.getTranslations().stream().noneMatch(
                                        t -> language.equalsIgnoreCase(t.getLanguage())
                                )) {
                                    existingAmenity.getTranslations().add(amenity.getTranslations().get(0));
                                }
                                updatedAmenities.add(savedAmenity.get());
                            } else {
                                updatedAmenities.add(amenityRepository.save(amenity));
                            }
                        }
                        currentRoomSavedRoom.setRoomAmenities(updatedAmenities);

                    }
                }
                if (Objects.nonNull(hotel.getPolicies()) && !hotel.getPolicies().isEmpty()) {

                    Map<String, Policy> policyMap = retrievedHotel.getPolicies().stream()
                            .collect(Collectors.toMap(Policy::getPolicyType, p -> p));

                    for (Policy policy : hotel.getPolicies()) {
                        Policy matchingPolicy = policyMap.get(policy.getPolicyType());

                        if (matchingPolicy != null) {
                            policy = policyMap.get(policy.getPolicyType());
                        }
                    }

                }
                if (Objects.nonNull(hotel.getFacilities()) && !hotel.getFacilities().isEmpty()) {

                    List<Facility> updatedFacilities = new ArrayList<>();
                    for (Facility facility : hotel.getFacilities()) {
                        Optional<Facility> savedFacility = facilityRepository.findByFacilityId(facility.getFacilityId());
                        if (savedFacility.isPresent()) {
                            Facility existingFacility = savedFacility.get();
                            if (existingFacility.getTranslations().stream().noneMatch(
                                    t -> language.equalsIgnoreCase(t.getLanguage())
                            )) {
                                existingFacility.getTranslations().add(facility.getTranslations().get(0));
                            }
                            updatedFacilities.add(savedFacility.get());
                        } else {
                            updatedFacilities.add(facilityRepository.save(facility));
                        }
                    }
                    hotel.setFacilities(updatedFacilities);
                }
                hotel = retrievedHotel;
            }
        }

        else {
            // Reusing pre-existing Room Amenities and Facilities.

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

        }

        // Reusing existing Policies
        if (Objects.nonNull(hotel.getPolicies()) && !hotel.getPolicies().isEmpty()) {
            List<Policy> updatedPolicies = new ArrayList<>();
            for (Policy policy : hotel.getPolicies()) {
                Optional<Policy> savedPolicy = policyRepository.findByPolicyType(policy.getPolicyType());
                updatedPolicies.add(savedPolicy.orElse(policy));
            }
            hotel.setPolicies(updatedPolicies);
        }

        if(containsReviews)
            hotel.setReviews(reviewList);

        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> getHotelByIdWithTranslations(int hotelId, String language) {

        if (language == null)
            return hotelRepository.findById(hotelId);

        else {
            final String finalLanguage = language;

            Optional<Hotel> savedHotel = hotelRepository.findByIdWithTranslationsByLanguage(hotelId, finalLanguage);
            Hotel hotel = savedHotel.get();

            // Filter collections' translations
            for (Facility facility : hotel.getFacilities()) {
                Hibernate.initialize(facility.getTranslations());  // Ensure the translations are loaded
                facility.setTranslations(facility.getTranslations().stream()
                        .filter(t -> t.getLanguage().equals(finalLanguage))
                        .collect(Collectors.toList()));
            }

            hotel.setReviews(hotel.getReviews().stream()
                    .filter(t -> t.getLanguage().equals(finalLanguage))
                    .collect(Collectors.toList()));

            for (Room room : hotel.getRooms()) {
                Hibernate.initialize(room.getTranslations());  // Ensure the translations are loaded
                room.setTranslations(room.getTranslations().stream()
                        .filter(t -> t.getLanguage().equals(finalLanguage))
                        .collect(Collectors.toList()));

                for (Amenity amenity : room.getRoomAmenities()) {
                    Hibernate.initialize(amenity.getTranslations());  // Ensure the translations are loaded
                    amenity.setTranslations(amenity.getTranslations().stream()
                            .filter(t -> t.getLanguage().equals(finalLanguage))
                            .collect(Collectors.toList()));
                }
            }

            return savedHotel;
        }
    }

    public List<Review> addHotelReviews(Integer hotelId, List<ReviewDTO> reviewDTOList) {

        Optional<Hotel> savedHotel = hotelRepository.findById(hotelId);
        if (savedHotel.isEmpty())
            return null;

        List<Review> reviewList = new ArrayList<>();
        for (ReviewDTO reviewDto : reviewDTOList) {
            reviewList.add(Review.fromDTO(reviewDto));
        }

        savedHotel.get().setReviews(reviewList);
        hotelRepository.save(savedHotel.get());

        return reviewList;

    }

    public List<Review> getHotelReviews(Integer hotelId, String language) {

        List<Review> reviewList;

        if (Objects.nonNull(language))
            reviewList = hotelRepository.findReviewsByHotelIdAndLanguage(hotelId, language);
        else
            reviewList = hotelRepository.findReviewsByHotelId(hotelId);

        return reviewList;

    }
}