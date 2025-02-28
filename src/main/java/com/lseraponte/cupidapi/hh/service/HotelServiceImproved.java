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
            List<Facility> updatedFacilities = new ArrayList<>();
            updatedFacilities.addAll(retrievedFacilities);
            if (Objects.nonNull(hotelFacilities) && !hotelFacilities.isEmpty()) {
                for (Facility hotelCurrentFacility : hotelFacilities) {

                    Optional<Facility> retrievedCurrentFacilityOptional = retrievedFacilities.stream()
                            .filter(t -> hotelCurrentFacility.getFacilityId().equals(t.getFacilityId()))
                            .findFirst();

                    if (retrievedCurrentFacilityOptional.isPresent()) {
                        Facility retrievedCurrentFacility = retrievedCurrentFacilityOptional.get();
                        translationUpdated = false;

                        for (int i = 0; i < retrievedCurrentFacility.getTranslations().size(); i++) {
                            if (languageCode.equals(retrievedCurrentFacility.getTranslations().get(i).getLanguage())) {
                                retrievedCurrentFacility.getTranslations().set(i,
                                        facilityTranslationValues(hotelCurrentFacility.getTranslations().get(0),
                                                retrievedCurrentFacility.getTranslations().get(i)));
                                translationUpdated = true;
                                break;
                            }
                        }

                        if (!translationUpdated)
                            retrievedCurrentFacility.getTranslations().add(hotelCurrentFacility.getTranslations().get(0));
//                        updatedFacilities.add(retrievedCurrentFacility);
                    } else {
                        updatedFacilities.add(facilityRepository.save(hotelCurrentFacility));
                    }
                }
            }
            hotel.setFacilities(updatedFacilities);

            List<Photo> hotelPhotos = hotel.getPhotos();
            List<Photo> retrievedPhotos = retrievedHotel.getPhotos();
            List<Photo> updatedPhotos = new ArrayList<>();
            updatedPhotos.addAll(retrievedPhotos);
            if (Objects.nonNull(hotelPhotos) && !hotelPhotos.isEmpty()) {

                retrievedPhotos = retrievedPhotos.stream()
                        .filter(retirevedPhoto ->
                                hotelPhotos.stream()
                                        .anyMatch(hotelPhoto -> hotelPhoto.getUrl().equals(retirevedPhoto.getUrl())))
                        .collect(Collectors.toList());

                for (Photo hotelCurrentPhoto : hotelPhotos) {
                    Optional<Photo> retrievedCurrentPhotoOptional = retrievedPhotos.stream()
                            .filter(t -> hotelCurrentPhoto.getUrl().equals(t.getUrl()))
                            .findFirst();
                    if (retrievedCurrentPhotoOptional.isEmpty()) {
                        updatedPhotos.add(photoRepository.save(hotelCurrentPhoto));
                    }
                }
            }
            hotel.setPhotos(updatedPhotos);

            List<Policy> hotelPolicies = hotel.getPolicies();
            List<Policy> retrievedPolicies = retrievedHotel.getPolicies();
            List<Policy> updatedPolicies = new ArrayList<>();
            updatedPolicies.addAll(retrievedPolicies);
            if (Objects.nonNull(hotelPolicies) && !hotelPolicies.isEmpty()) {
                for (Policy hotelCurrentPolicy : hotelPolicies) {
                    Optional<Policy> retrievedCurrentPolicyOptional = retrievedPolicies.stream()
                            .filter(t -> hotelCurrentPolicy.getPolicyType().equals(t.getPolicyType()))
                            .findFirst();
                    if (retrievedCurrentPolicyOptional.isEmpty()) {
                        updatedPolicies.add(policyRepository.save(hotelCurrentPolicy));
                    }
                }
            }
            hotel.setPolicies(updatedPolicies);

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
