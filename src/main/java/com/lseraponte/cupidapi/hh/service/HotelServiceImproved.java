package com.lseraponte.cupidapi.hh.service;

import static com.lseraponte.cupidapi.hh.util.ClassMergingUtil.hotelTranslationValues;
import static com.lseraponte.cupidapi.hh.util.ClassMergingUtil.roomTranslationValues;
import static com.lseraponte.cupidapi.hh.util.ClassMergingUtil.amenityTranslationValues;
import static com.lseraponte.cupidapi.hh.util.ClassMergingUtil.facilityTranslationValues;
import static com.lseraponte.cupidapi.hh.util.ClassMergingUtil.photoValues;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import com.lseraponte.cupidapi.hh.model.Amenity;
import com.lseraponte.cupidapi.hh.model.Facility;
import com.lseraponte.cupidapi.hh.model.Hotel;
import com.lseraponte.cupidapi.hh.model.Photo;
import com.lseraponte.cupidapi.hh.model.Policy;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.model.Room;
import com.lseraponte.cupidapi.hh.repository.AmenityRepository;
import com.lseraponte.cupidapi.hh.repository.FacilityRepository;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import com.lseraponte.cupidapi.hh.repository.PhotoRepository;
import com.lseraponte.cupidapi.hh.repository.PolicyRepository;
import com.lseraponte.cupidapi.hh.repository.RoomRepository;
import com.lseraponte.cupidapi.hh.util.Language;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImproved {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final FacilityRepository facilityRepository;
    private final AmenityRepository amenityRepository;
    private final PolicyRepository policyRepository;
    private final PhotoRepository photoRepository;

    @Transactional
    public Hotel saveHotelWithTranslation(HotelDTO hotelDTO, String language, List<ReviewDTO> reviewDTOList) {

        Language langEnum = Language.fromString(language);
        final String languageCode = langEnum.getCode();

        Hotel hotel = Hotel.fromDTO(hotelDTO, languageCode);
        boolean containsReviews = Objects.nonNull(reviewDTOList);
        List<Review> reviewList = containsReviews ? reviewDTOList.stream().map(Review::fromDTO)
                .collect(Collectors.toList()) : new ArrayList<>();

        Optional<Hotel> savedHotel = hotelRepository.findById(hotel.getHotelId());
        if (savedHotel.isPresent()) {
            Hotel retrievedHotel = savedHotel.get();

            boolean translationUpdated = false;
            for (int i = 0; i < retrievedHotel.getTranslations().size(); i++) {
                if (languageCode.equals(retrievedHotel.getTranslations().get(i).getLanguage())) {
                    retrievedHotel.getTranslations().set(i,
                            hotelTranslationValues(hotel.getTranslations().get(0),
                                    retrievedHotel.getTranslations().get(i)));
                    translationUpdated = true;
                    break;
                }
            }

            if (!translationUpdated)
                retrievedHotel.getTranslations().add(hotel.getTranslations().get(0));

            hotel.setTranslations(retrievedHotel.getTranslations());

            if (containsReviews) {
                retrievedHotel.setReviews(reviewList);
            }

            if (Objects.nonNull(hotel.getRooms())) {

                List<Room> hotelCurrentRooms = hotel.getRooms();
                List<Room> retrievedCurrentRooms = retrievedHotel.getRooms();
                List<Room> updatedRooms = new ArrayList<>();
                updatedRooms.addAll(retrievedCurrentRooms);
                for (Room hotelCurrentRoom : hotelCurrentRooms) {
                    Optional<Room> retrievedCurrentRoomOptional = retrievedCurrentRooms.stream()
                            .filter(t -> hotelCurrentRoom.getId().equals(t.getId()))
                            .findFirst();

                    if (retrievedCurrentRoomOptional.isPresent()) {
                        Room retrievedCurrentRoom = retrievedCurrentRoomOptional.get();
                        translationUpdated = false;

                        for (int i = 0; i < retrievedCurrentRoom.getTranslations().size(); i++) {
                            if (languageCode.equals(retrievedCurrentRoom.getTranslations().get(i).getLanguage())) {
                                retrievedCurrentRoom.getTranslations().set(i,
                                        roomTranslationValues(hotelCurrentRoom.getTranslations().get(0),
                                                retrievedCurrentRoom.getTranslations().get(i)));
                                translationUpdated = true;
                                break;
                            }
                        }

                        if (!translationUpdated)
                            retrievedCurrentRoom.getTranslations().add(hotelCurrentRoom.getTranslations().get(0));
                        updatedRooms.add(retrievedCurrentRoom);

                        List<Amenity> hotelCurrentAmenities = hotelCurrentRoom.getRoomAmenities();
                        List<Amenity> retrievedCurrentAmenities = retrievedCurrentRoom.getRoomAmenities();
                        List<Amenity> updatedAmenities = new ArrayList<>();
                        updatedAmenities.addAll(retrievedCurrentAmenities);
                        for (Amenity hotelCurrentAmenity : hotelCurrentAmenities) {

                            Optional<Amenity> retrievedCurrentAmenityOptional = retrievedCurrentAmenities.stream()
                                    .filter(t -> hotelCurrentAmenity.getAmenityId().equals(t.getAmenityId()))
                                    .findFirst();

                            if (retrievedCurrentAmenityOptional.isPresent()) {
                                Amenity retrievedCurrentAmenity = retrievedCurrentAmenityOptional.get();
                                translationUpdated = false;
                                for (int i = 0; i < retrievedCurrentAmenity.getTranslations().size(); i++) {
                                    if (languageCode.equals(retrievedCurrentAmenity.getTranslations().get(i).getLanguage())) {
                                        retrievedCurrentAmenity.getTranslations().set(i,
                                                amenityTranslationValues(hotelCurrentAmenity.getTranslations().get(0),
                                                        retrievedCurrentAmenity.getTranslations().get(i)));
                                        translationUpdated = true;
                                        break;
                                    }
                                }
                                if (!translationUpdated)
                                    retrievedCurrentAmenity.getTranslations().add(hotelCurrentAmenity.getTranslations().get(0));
                            }
                            else {
                                updatedAmenities.add(amenityRepository.save(hotelCurrentAmenity));
                            }
                        }
                        hotelCurrentRoom.setRoomAmenities(updatedAmenities);

                        List<Photo> hotelCurrentPhotos = hotelCurrentRoom.getPhotos();
                        List<Photo> retrievedCurrentPhotos = retrievedCurrentRoom.getPhotos();
                        List<Photo> updatedPhotos = new ArrayList<>();
                        updatedPhotos.addAll(retrievedCurrentPhotos);
                        for (Photo hotelCurrentPhoto : hotelCurrentPhotos) {
                            Optional<Photo> retrievedCurrentPhotoOptional = retrievedCurrentPhotos.stream()
                                    .filter(t -> hotelCurrentPhoto.getUrl().equals(t.getUrl()))
                                    .findFirst();
                            if (retrievedCurrentPhotoOptional.isEmpty()) {
                                updatedPhotos.add(photoRepository.save(hotelCurrentPhoto));
                            }
                        }
                        hotelCurrentRoom.setPhotos(updatedPhotos);
                    }
                    else {
                        updatedRooms.add(roomRepository.save(hotelCurrentRoom));
                    }
                }
                hotel.setRooms(updatedRooms);
            }

            List<Facility> hotelFacilities = hotel.getFacilities();
            List<Facility> retrievedFacilities = retrievedHotel.getFacilities();
            if (Objects.nonNull(hotelFacilities) && !hotelFacilities.isEmpty()) {
                // Remove facilities that are not present in the incoming request
                List<Facility> filteredFacilities = retrievedFacilities.stream()
                        .filter(retrievedFacility -> hotelFacilities.stream()
                                .anyMatch(hotelFacility -> hotelFacility.getFacilityId().equals(retrievedFacility.getFacilityId())))
                        .collect(Collectors.toList());

                // Add or update facilities
                for (Facility hotelCurrentFacility : hotelFacilities) {
                    Optional<Facility> retrievedCurrentFacilityOptional = filteredFacilities.stream()
                            .filter(t -> hotelCurrentFacility.getFacilityId().equals(t.getFacilityId()))
                            .findFirst();

                    if (retrievedCurrentFacilityOptional.isEmpty()) {
                        filteredFacilities.add(facilityRepository.save(hotelCurrentFacility));
                    } else {
                        Facility existingFacility = retrievedCurrentFacilityOptional.get();
                        translationUpdated = false;

                        for (int i = 0; i < existingFacility.getTranslations().size(); i++) {
                            if (languageCode.equals(existingFacility.getTranslations().get(i).getLanguage())) {
                                existingFacility.getTranslations().set(i,
                                        facilityTranslationValues(hotelCurrentFacility.getTranslations().get(0),
                                                existingFacility.getTranslations().get(i)));
                                translationUpdated = true;
                                break;
                            }
                        }

                        if (!translationUpdated) {
                            existingFacility.getTranslations().add(hotelCurrentFacility.getTranslations().get(0));
                        }
                    }
                }
                hotel.setFacilities(filteredFacilities);
            }

            List<Photo> hotelPhotos = hotel.getPhotos();
            List<Photo> retrievedPhotos = retrievedHotel.getPhotos();
            if (Objects.nonNull(hotelPhotos) && !hotelPhotos.isEmpty()) {
                // Remove photos that are not present in the incoming request
                List<Photo> filteredPhotos = retrievedPhotos.stream()
                        .filter(retrievedPhoto -> hotelPhotos.stream()
                                .anyMatch(hotelPhoto -> hotelPhoto.getUrl().equals(retrievedPhoto.getUrl())))
                        .collect(Collectors.toList());

                // Add or update photos
                for (Photo hotelCurrentPhoto : hotelPhotos) {
                    Optional<Photo> retrievedCurrentPhotoOptional = filteredPhotos.stream()
                            .filter(t -> hotelCurrentPhoto.getUrl().equals(t.getUrl()))
                            .findFirst();

                    if (retrievedCurrentPhotoOptional.isEmpty()) {
                        filteredPhotos.add(photoRepository.save(hotelCurrentPhoto));
                    } else {
                        Photo existingPhoto = photoValues(hotelCurrentPhoto, retrievedCurrentPhotoOptional.get()); // Example update
                    }
                }

                hotel.setPhotos(filteredPhotos);
            }

            List<Policy> hotelPolicies = hotel.getPolicies();
            List<Policy> retrievedPolicies = retrievedHotel.getPolicies();
            if (Objects.nonNull(hotelPolicies) && !hotelPolicies.isEmpty()) {
                // Remove policies that are not present in the incoming request
                List<Policy> filteredPolicies = retrievedPolicies.stream()
                        .filter(retrievedPolicy -> hotelPolicies.stream()
                                .anyMatch(hotelPolicy -> hotelPolicy.getPolicyType().equals(retrievedPolicy.getPolicyType())))
                        .collect(Collectors.toList());
                // Add or update policies
                for (Policy hotelCurrentPolicy : hotelPolicies) {
                    Optional<Policy> retrievedCurrentPolicyOptional = filteredPolicies.stream()
                            .filter(t -> hotelCurrentPolicy.getPolicyType().equals(t.getPolicyType()))
                            .findFirst();

                    if (retrievedCurrentPolicyOptional.isEmpty()) {
                        filteredPolicies.add(policyRepository.save(hotelCurrentPolicy));
                    } else {
                        Policy existingPolicy = retrievedCurrentPolicyOptional.get();
                        existingPolicy.setDescription(hotelCurrentPolicy.getDescription()); // Example update
                    }
                }

                hotel.setPolicies(filteredPolicies);
            }



        } else {
            for (Room room : hotel.getRooms()) {
                List<Amenity> updatedAmenities = new ArrayList<>();
                for (Amenity amenity : room.getRoomAmenities()) {
                    Amenity existingAmenity = amenityRepository.findByAmenityId(amenity.getAmenityId())
                            .orElseGet(() -> amenityRepository.save(amenity));
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

        if (Objects.nonNull(hotel.getPolicies()) && !hotel.getPolicies().isEmpty()) {
            List<Policy> updatedPolicies = new ArrayList<>();
            for (Policy policy : hotel.getPolicies()) {
                Optional<Policy> savedPolicy = policyRepository.findByPolicyType(policy.getPolicyType());
                updatedPolicies.add(savedPolicy.orElse(policy));
            }
            hotel.setPolicies(updatedPolicies);
        }

        if (containsReviews) {
            hotel.setReviews(reviewList);
        }

        return hotelRepository.save(hotel);
    }


}
