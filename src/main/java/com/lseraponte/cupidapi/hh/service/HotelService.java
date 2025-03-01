package com.lseraponte.cupidapi.hh.service;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.HotelWithTranslationDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.BedType;
import com.lseraponte.cupidapi.hh.model.BedTypeTranslation;
import com.lseraponte.cupidapi.hh.model.Facility;
import com.lseraponte.cupidapi.hh.model.FacilityTranslation;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Policy;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.model.Room;
import com.lseraponte.cupidapi.hh.repository.AmenityRepository;
import com.lseraponte.cupidapi.hh.repository.BedTypeRepository;
import com.lseraponte.cupidapi.hh.repository.FacilityRepository;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import com.lseraponte.cupidapi.hh.repository.PolicyRepository;
import com.lseraponte.cupidapi.hh.util.Language;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    private final HotelServiceImproved hotelServiceImproved;
    private final HotelRepository hotelRepository;
    private final FacilityRepository facilityRepository;
    private final AmenityRepository amenityRepository;
    private final PolicyRepository policyRepository;
    private final BedTypeRepository bedTypeRepository;

    @Transactional
    public Hotel saveHotelWithTranslation(HotelDTO hotelDTO, String language, List<ReviewDTO> reviewDTOList) {

        Language langEnum = Language.fromString(language);
        final String languageCode = langEnum.getCode();

        Optional<Hotel> savedHotelWithLanguage = hotelRepository.findByIdWithTranslationsByLanguage(hotelDTO.hotelId(), languageCode);
        if(savedHotelWithLanguage.isPresent())
            return savedHotelWithLanguage.get();

        Optional<Hotel> savedHotel = hotelRepository.findById(hotelDTO.hotelId());
        if(savedHotel.isPresent())
            return savedHotel.get();

        Hotel hotel = Hotel.fromDTO(hotelDTO, languageCode);
        boolean containsReviews = false;
        List<Review> reviewList = new ArrayList<>();;

        if (Objects.nonNull(reviewDTOList)) {
            containsReviews = true;
            for (ReviewDTO reviewDto : reviewDTOList) {
                reviewList.add(Review.fromDTO(reviewDto));
            }
        }

        // Reusing pre-existing Room Amenities and BedTypes.
        for (Room room : hotel.getRooms()) {
            List<Amenity> updatedAmenities = new ArrayList<>();
            for (Amenity amenity : room.getRoomAmenities()) {
                Amenity existingAmenity = amenityRepository.findByAmenityId(amenity.getAmenityId())
                        .orElseGet(() -> amenityRepository.save(amenity));
                updatedAmenities.add(existingAmenity);
            }
            room.setRoomAmenities(updatedAmenities);
            List<BedType> updatedBedTypes = new ArrayList<>();
            for (BedType bedType : room.getBedTypes()) {
                BedTypeTranslation currentBedTypeTranslation = bedType.getTranslations().get(0);
                BedType existingBedType = bedTypeRepository.findByBedTypeAndBedSize(currentBedTypeTranslation.getBedTypeName(), currentBedTypeTranslation.getBedSize())
                        .orElseGet(() -> bedTypeRepository.save(bedType));
                updatedBedTypes.add(existingBedType);
            }
            room.setBedTypes(updatedBedTypes);
        }
        // Reusing pre-existing Facilities
        if (Objects.nonNull(hotel.getFacilities()) && !hotel.getFacilities().isEmpty()) {
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
                Optional<Policy> savedPolicy = policyRepository.findByPolicyTypeAndNameAndDescription(
                        policy.getPolicyType(),
                        policy.getName(),
                        policy.getDescription());
                updatedPolicies.add(savedPolicy.orElse(policy));
            }
            hotel.setPolicies(updatedPolicies);
        }

        if(containsReviews)
            hotel.setReviews(reviewList);

        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel (HotelDTO hotelDTO, List<ReviewDTO> reviewDTOList, String language) {

        Language langEnum = Language.fromString(language);
        final String languageCode = langEnum.getCode();

        return hotelServiceImproved.updateHotelWithTranslation(hotelDTO, languageCode, reviewDTOList);

    }

    public Optional<Hotel> getHotelByIdWithTranslations(int hotelId, String language) {

        Language langEnum = Language.fromString(language);
        final String languageCode = langEnum.getCode();

        Optional<Hotel> savedHotel = hotelRepository.findByIdWithTranslationsByLanguage(hotelId, languageCode);

        if (savedHotel.isPresent()) {
            Hotel hotel = savedHotel.get();
            filterHotelProperties(hotel, languageCode);
        }

        return savedHotel;
    }

    public Optional<Hotel> getHotelByNameWithTranslationsByLanguage(String hotelName, String language) {

        Language langEnum = Language.fromString(language);
        final String languageCode = langEnum.getCode();

        Optional<Hotel> savedHotel = hotelRepository.findByNameWithTranslationsByLanguage(hotelName, languageCode);

        savedHotel.ifPresent(hotel -> filterHotelProperties(hotel, languageCode));

        return savedHotel;
    }

    public Optional<List<HotelWithTranslationDTO>> getHotelsByCityWithTranslationsByLanguage(String city, String language) {

        Language langEnum = Language.fromString(language);
        final String languageCode = langEnum.getCode();

        return hotelRepository.findHotelsWithTranslationsByCityAndLanguage(city, languageCode);
    }

    public void deleteHotelById(Integer hotelId) {
        if (!hotelRepository.existsById(hotelId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found");
        }
        hotelRepository.deleteById(hotelId);
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

        Language langEnum = Language.fromString(language);
        final String languageCode = langEnum.getCode();

        return hotelRepository.findReviewsByHotelIdAndLanguage(hotelId, languageCode);
    }

    private void filterHotelProperties(Hotel hotel, String language) {

        hotel.setFacilities(hotel.getFacilities().stream()
                .filter(facility -> {
                    Hibernate.initialize(facility.getTranslations());

                    // Filter translations by language
                    List<FacilityTranslation> filteredTranslations = facility.getTranslations().stream()
                            .filter(t -> t.getLanguage().equals(language))
                            .collect(Collectors.toList());

                    facility.setTranslations(filteredTranslations); // Update translations

                    return !filteredTranslations.isEmpty(); // Only keep facilities that have translations
                })
                .collect(Collectors.toList()));

        hotel.setReviews(hotel.getReviews().stream()
                .filter(t -> t.getLanguage().equals(language))
                .collect(Collectors.toList()));

        for (Room room : hotel.getRooms()) {
            Hibernate.initialize(room.getTranslations());
            room.setTranslations(room.getTranslations().stream()
                    .filter(t -> t.getLanguage().equals(language))
                    .collect(Collectors.toList()));

            for (Amenity amenity : room.getRoomAmenities()) {
                Hibernate.initialize(amenity.getTranslations());
                amenity.setTranslations(amenity.getTranslations().stream()
                        .filter(t -> t.getLanguage().equals(language))
                        .collect(Collectors.toList()));
            }
        }
    }
}