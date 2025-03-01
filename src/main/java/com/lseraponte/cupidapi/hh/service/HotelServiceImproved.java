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
    public Hotel updateHotelWithTranslation(HotelDTO hotelDTO, String language, List<ReviewDTO> reviewDTOList) {

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

            hotel.setFacilities(updateFacilities(hotel, languageCode));
            hotel.setPhotos(updatePhotos(hotel, languageCode));
            hotel.setPolicies(updatePolicies(hotel, languageCode));
            hotel.setRooms(updateRooms(hotel, languageCode));

        }
        return hotelRepository.save(hotel);
    }

    private List<Room> updateRooms(Hotel hotel, String languageCode) {

        List<Room> hotelCurrentRooms = hotel.getRooms();
        List<Room> retrievedCurrentRooms = hotelRepository.findRoomByHotelId(hotel.getHotelId());

        List<Room> filteredRooms = retrievedCurrentRooms.stream()
                .filter(retrievedRoom -> hotelCurrentRooms.stream()
                        .anyMatch(hotelRoom -> hotelRoom.getId().equals(retrievedRoom.getId())))
                .collect(Collectors.toList());

        if (Objects.nonNull(hotelCurrentRooms) && !hotelCurrentRooms.isEmpty()) {
            for (Room hotelCurrentRoom : hotelCurrentRooms) {

                hotelCurrentRoom.setRoomAmenities(updateAmenities(hotelCurrentRoom, languageCode));
                hotelCurrentRoom.setPhotos(updatePhotos(hotelCurrentRoom, languageCode));

                Optional<Room> retrievedCurrentRoomOptional = retrievedCurrentRooms.stream()
                        .filter(t -> hotelCurrentRoom.getId().equals(t.getId()))
                        .findFirst();

                if (retrievedCurrentRoomOptional.isEmpty()) {
                    filteredRooms.add(roomRepository.save(hotelCurrentRoom));
                } else {
                    Room existingRoom = retrievedCurrentRoomOptional.get();
                    boolean translationUpdated = false;

                    for (int i = 0; i < existingRoom.getTranslations().size(); i++) {
                        if (languageCode.equals(existingRoom.getTranslations().get(i).getLanguage())) {
                            existingRoom.getTranslations().set(i,
                                    roomTranslationValues(hotelCurrentRoom.getTranslations().get(0),
                                            existingRoom.getTranslations().get(i)));
                            translationUpdated = true;
                            break;
                        }
                    }

                    if (!translationUpdated) {
                        existingRoom.getTranslations().add(hotelCurrentRoom.getTranslations().get(0));
                    }
                }
            }
            return filteredRooms;
        }
        return null;
    }

    private List<Amenity> updateAmenities(Room room, String languageCode) {

        List<Amenity> hotelCurrentAmenities = room.getRoomAmenities();
        List<Amenity> retrievedCurrentAmenities = roomRepository.findAmenityByRoomId(room.getId());

        List<Amenity> filteredAmenities = retrievedCurrentAmenities.stream()
                .filter(retrievedAmenity -> hotelCurrentAmenities.stream()
                        .anyMatch(hotelAmenity -> hotelAmenity.getAmenityId().equals(retrievedAmenity.getAmenityId())))
                .collect(Collectors.toList());

        if (Objects.nonNull(hotelCurrentAmenities) && !hotelCurrentAmenities.isEmpty()) {
            for (Amenity hotelCurrentAmenity : hotelCurrentAmenities) {

                Optional<Amenity> retrievedCurrentAmenityOptional = retrievedCurrentAmenities.stream()
                        .filter(t -> hotelCurrentAmenity.getAmenityId().equals(t.getAmenityId()))
                        .findFirst();

                if (retrievedCurrentAmenityOptional.isEmpty()) {
                    filteredAmenities.add(amenityRepository.save(hotelCurrentAmenity));
                } else {
                    Amenity existingAmenity = retrievedCurrentAmenityOptional.get();
                    boolean translationUpdated = false;

                    for (int i = 0; i < existingAmenity.getTranslations().size(); i++) {
                        if (languageCode.equals(existingAmenity.getTranslations().get(i).getLanguage())) {
                            existingAmenity.getTranslations().set(i,
                                    amenityTranslationValues(hotelCurrentAmenity.getTranslations().get(0),
                                            existingAmenity.getTranslations().get(i)));
                            translationUpdated = true;
                            break;
                        }
                    }
                    if (!translationUpdated) {
                        existingAmenity.getTranslations().add(hotelCurrentAmenity.getTranslations().get(0));
                    }
                }
            }
            return filteredAmenities;
        }
        return null;
    }

    private List<Facility> updateFacilities(Hotel hotel, String languageCode) {

        List<Facility> hotelFacilities = hotel.getFacilities();
        List<Facility> retrievedFacilities = hotelRepository.findFacilitiesByHotelId(hotel.getHotelId());

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
                    boolean translationUpdated = false;

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
            return filteredFacilities;
        }
        return null;
    }

    private List<Photo> updatePhotos(Object entity, String languageCode) {

        List<Photo> photos;
        List<Photo> retrievedPhotos = new ArrayList<>();

        if (entity instanceof Hotel hotel) {
            photos = hotel.getPhotos();
            retrievedPhotos = hotelRepository.findPhotosByHotelId(hotel.getHotelId());
        } else if (entity instanceof Room room) {
            photos = room.getPhotos();
            retrievedPhotos = roomRepository.findPhotosByRoomId(room.getId());
        } else {
            photos = null;
            throw new IllegalArgumentException("Unsupported entity type");
        }

        if (Objects.nonNull(photos) && !photos.isEmpty()) {

            List<Photo> filteredPhotos = retrievedPhotos.stream()
                    .filter(retrievedPhoto -> photos.stream()
                            .anyMatch(photo -> photo.getUrl().equals(retrievedPhoto.getUrl())))
                    .collect(Collectors.toList());

            for (Photo currentPhoto : photos) {
                Optional<Photo> retrievedCurrentPhotoOptional = filteredPhotos.stream()
                        .filter(t -> currentPhoto.getUrl().equals(t.getUrl()))
                        .findFirst();

                if (retrievedCurrentPhotoOptional.isEmpty()) {
                    filteredPhotos.add(photoRepository.save(currentPhoto));
                } else {
                    Photo existingPhoto = photoValues(currentPhoto, retrievedCurrentPhotoOptional.get());
                }
            }

            return(filteredPhotos);
        }
        return null;
    }

    private List<Policy> updatePolicies(Hotel hotel, String languageCode) {

        List<Policy> hotelPolicies = hotel.getPolicies();
        List<Policy> retrievedPolicies = hotelRepository.findPolicyByHotelId(hotel.getHotelId());

        if (Objects.nonNull(hotelPolicies) && !hotelPolicies.isEmpty()) {

            List<Policy> filteredPolicies = retrievedPolicies.stream()
                    .filter(retrievedPolicy -> hotelPolicies.stream()
                            .anyMatch(hotelPolicy -> hotelPolicy.getPolicyType().equals(retrievedPolicy.getPolicyType())))
                    .collect(Collectors.toList());

            for (Policy hotelCurrentPolicy : hotelPolicies) {
                Optional<Policy> retrievedCurrentPolicyOptional = filteredPolicies.stream()
                        .filter(t -> hotelCurrentPolicy.getPolicyId().equals(t.getPolicyId()))
                        .findFirst();

                if (retrievedCurrentPolicyOptional.isEmpty()) {
                    filteredPolicies.add(policyRepository.save(hotelCurrentPolicy));
                } else {
                    Policy existingPolicy = retrievedCurrentPolicyOptional.get();
                    existingPolicy.setDescription(hotelCurrentPolicy.getDescription());
                }
            }
            return filteredPolicies;
        }
        return null;
    }
}
