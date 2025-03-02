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
import com.lseraponte.cupidapi.hh.model.Photo;
import com.lseraponte.cupidapi.hh.model.Policy;
import com.lseraponte.cupidapi.hh.model.Review;
import com.lseraponte.cupidapi.hh.model.Room;
import com.lseraponte.cupidapi.hh.repository.AmenityRepository;
import com.lseraponte.cupidapi.hh.repository.BedTypeRepository;
import com.lseraponte.cupidapi.hh.repository.FacilityRepository;
import com.lseraponte.cupidapi.hh.repository.HotelRepository;
import com.lseraponte.cupidapi.hh.repository.PhotoRepository;
import com.lseraponte.cupidapi.hh.repository.PolicyRepository;
import com.lseraponte.cupidapi.hh.repository.RoomRepository;
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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.lseraponte.cupidapi.hh.util.ClassMergingUtil.*;
import static com.lseraponte.cupidapi.hh.util.ClassMergingUtil.photoValues;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final FacilityRepository facilityRepository;
    private final AmenityRepository amenityRepository;
    private final BedTypeRepository bedTypeRepository;
    private final PolicyRepository policyRepository;
    private final PhotoRepository photoRepository;

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
                BedType existingBedType = bedTypeRepository.findByBedTypeAndBedSizeAndLanguage(
                        currentBedTypeTranslation.getBedTypeName(), currentBedTypeTranslation.getBedSize(),
                                currentBedTypeTranslation.getLanguage())
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

    @Transactional
    public Hotel updateHotel (HotelDTO hotelDTO, List<ReviewDTO> reviewDTOList, String language) {

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

            if (Objects.nonNull(retrievedHotel.getReviews()))
                reviewList.addAll(retrievedHotel.getReviews());

            hotel.setReviews(reviewList);
            hotel.setFacilities(updateFacilities(hotel, languageCode));
            hotel.setPhotos(updatePhotos(hotel, languageCode));
            hotel.setPolicies(updatePolicies(hotel, languageCode));
            hotel.setRooms(updateRooms(hotel, languageCode));
            hotel.setTranslations(retrievedHotel.getTranslations());
        }
        return hotelRepository.save(hotel);

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

    private List<Room> updateRooms(Hotel hotel, String languageCode) {

        List<Room> hotelCurrentRooms = hotel.getRooms();
        List<Room> retrievedCurrentRooms = hotelRepository.findRoomByHotelId(hotel.getHotelId());
        List<Room>  updatedRoomList = new ArrayList<>();

        if (Objects.nonNull(hotelCurrentRooms) && !hotelCurrentRooms.isEmpty()) {
            for (Room currentRoom : hotelCurrentRooms) {
                currentRoom.setRoomAmenities(updateAmenities(currentRoom, languageCode));
                currentRoom.setBedTypes(updateBedType(currentRoom, languageCode));
                currentRoom.setPhotos(updatePhotos(currentRoom, languageCode));

                Optional<Room> retrievedCurrentRoomOptional = retrievedCurrentRooms.stream()
                        .filter(t -> t.getId().equals(currentRoom.getId()))
                        .findFirst();
                if (retrievedCurrentRoomOptional.isPresent()) {
                    Room existingRoom = retrievedCurrentRoomOptional.get();
                    boolean translationUpdated = false;
                    for (int i = 0; i < existingRoom.getTranslations().size(); i++) {
                        if (languageCode.equals(existingRoom.getTranslations().get(i).getLanguage())) {
                            existingRoom.getTranslations().set(i,
                                    roomTranslationValues(currentRoom.getTranslations().get(0),
                                            existingRoom.getTranslations().get(i)));
                            translationUpdated = true;
                            break;
                        }
                    }
                    if (!translationUpdated) {
                        existingRoom.getTranslations().add(currentRoom.getTranslations().get(0));
                    }
                    roomRepository.save(existingRoom);
                    updatedRoomList.add(existingRoom);
                }
                else
                    updatedRoomList.add(roomRepository.save(currentRoom));
            }
        }
        return updatedRoomList;
    }

    private List<Amenity> updateAmenities(Room room, String languageCode) {

        List<Amenity> hotelCurrentAmenities = room.getRoomAmenities();
        List<Amenity> retrievedCurrentAmenities = roomRepository.findAmenityByRoomId(room.getId());
        List<Amenity> updatedAmenityList = new ArrayList<>();

        if (Objects.nonNull(hotelCurrentAmenities) && !hotelCurrentAmenities.isEmpty()) {
            for (Amenity currentAmenity : hotelCurrentAmenities) {
                Optional<Amenity> retrievedCurrentAmenityOptional = retrievedCurrentAmenities.stream()
                        .filter(t -> t.getAmenityId().equals(currentAmenity.getAmenityId()))
                        .findFirst();
                if (retrievedCurrentAmenityOptional.isPresent()) {
                    Amenity existingAmenity = retrievedCurrentAmenityOptional.get();
                    boolean translationUpdated = false;
                    for (int i = 0; i < existingAmenity.getTranslations().size(); i++) {
                        if (languageCode.equals(existingAmenity.getTranslations().get(i).getLanguage())) {
                            existingAmenity.getTranslations().set(i,
                                    amenityTranslationValues(currentAmenity.getTranslations().get(0),
                                            existingAmenity.getTranslations().get(i)));
                            translationUpdated = true;
                            break;
                        }
                    }
                    if (!translationUpdated) {
                        existingAmenity.getTranslations().add(currentAmenity.getTranslations().get(0));
                    }
                    amenityRepository.save(existingAmenity);
                    updatedAmenityList.add(existingAmenity);
                }
                else
                    updatedAmenityList.add(amenityRepository.save(currentAmenity));
            }

            List<Amenity> existingAmenityOtherLanguage = retrievedCurrentAmenities.stream()
                    .filter(retrievedAmenity -> retrievedAmenity.getTranslations().stream()
                            .noneMatch(amenityTranslation -> languageCode.equals(amenityTranslation.getLanguage())))
                    .collect(Collectors.toList());

            updatedAmenityList.addAll(existingAmenityOtherLanguage);
        }
        return updatedAmenityList;
    }

    private List<BedType> updateBedType(Room room, String languageCode) {

        List<BedType> hotelCurrentBedTypes = room.getBedTypes();
        List<BedType> retrievedCurrentBedTypes = roomRepository.findBedTypeByRoomId(room.getId());
        List<BedType> updatedBedTypeList = new ArrayList<>();

        if (Objects.nonNull(hotelCurrentBedTypes) && !hotelCurrentBedTypes.isEmpty()) {
            for (BedType currentBedType : hotelCurrentBedTypes) {


                Optional<BedType> retrievedCurrentBedTypeOptional = retrievedCurrentBedTypes.stream()
                        .filter(t -> {
                            List<BedTypeTranslation> currentTranslations = currentBedType.getTranslations();
                            List<BedTypeTranslation> retrievedTranslations = t.getTranslations();

                            if (currentTranslations.size() != retrievedTranslations.size()) {
                                return false;
                            }

                            // Sort both lists based on bedTypeName and bedSize
                            currentTranslations.sort(Comparator.comparing(BedTypeTranslation::getBedTypeName)
                                    .thenComparing(BedTypeTranslation::getBedSize));
                            retrievedTranslations.sort(Comparator.comparing(BedTypeTranslation::getBedTypeName)
                                    .thenComparing(BedTypeTranslation::getBedSize));

                            return IntStream.range(0, currentTranslations.size())
                                    .allMatch(i -> currentTranslations.get(i).equals(retrievedTranslations.get(i)));
                        })
                        .findFirst();


                if (retrievedCurrentBedTypeOptional.isPresent()) {
                    BedType existingBedType = retrievedCurrentBedTypeOptional.get();
                    boolean translationUpdated = false;
                    for (int i = 0; i < existingBedType.getTranslations().size(); i++) {
                        if (languageCode.equals(existingBedType.getTranslations().get(i).getLanguage())) {
                            existingBedType.getTranslations().set(i,
                                    bedTypeTranslationValues(currentBedType.getTranslations().get(0),
                                            existingBedType.getTranslations().get(i)));
                            translationUpdated = true;
                            break;
                        }
                    }
                    if (!translationUpdated) {
                        existingBedType.getTranslations().add(currentBedType.getTranslations().get(0));
                    }
                    bedTypeRepository.save(existingBedType);
                    updatedBedTypeList.add(existingBedType);
                }
                else {
                    BedTypeTranslation bedTypeTranslation = currentBedType.getTranslations().get(0);
                    Optional<BedType> availableSavedBedType = bedTypeRepository
                            .findByBedTypeAndBedSizeAndLanguage(bedTypeTranslation.getBedTypeName(),
                                    bedTypeTranslation.getBedSize(), bedTypeTranslation.getLanguage());
                    availableSavedBedType.ifPresentOrElse(
                            updatedBedTypeList::add,
                            () -> updatedBedTypeList.add(bedTypeRepository.save(currentBedType))
                    );
                }
            }

//            List<BedType> existingBedTypeOtherLanguage = retrievedCurrentBedTypes.stream()
//                    .filter(retrievedBedType -> retrievedBedType.getTranslations().stream()
//                            .noneMatch(amenityTranslation -> languageCode.equals(amenityTranslation.getLanguage())))
//                    .collect(Collectors.toList());
//
//            updatedBedTypeList.addAll(existingBedTypeOtherLanguage);
        }
        return updatedBedTypeList;
    }

    private List<Facility> updateFacilities(Hotel hotel, String languageCode) {

        List<Facility> hotelFacilities = hotel.getFacilities();
        List<Facility> retrievedFacilities = hotelRepository.findFacilitiesByHotelId(hotel.getHotelId());
        List<Facility> updatedFacilityList = new ArrayList<>();

        if (Objects.nonNull(hotelFacilities) && !hotelFacilities.isEmpty()) {
            for (Facility currentFacility : hotelFacilities) {
                Optional<Facility> retrievedCurrentFacilityOptional = retrievedFacilities.stream()
                        .filter(t -> t.getFacilityId().equals(currentFacility.getFacilityId()))
                        .findFirst();
                if (retrievedCurrentFacilityOptional.isPresent()) {
                    Facility existingFacility = retrievedCurrentFacilityOptional.get();
                    boolean translationUpdated = false;
                    for (int i = 0; i < existingFacility.getTranslations().size(); i++) {
                        if (languageCode.equals(existingFacility.getTranslations().get(i).getLanguage())) {
                            existingFacility.getTranslations().set(i,
                                    facilityTranslationValues(currentFacility.getTranslations().get(0),
                                            existingFacility.getTranslations().get(i)));
                            translationUpdated = true;
                            break;
                        }
                    }
                    if (!translationUpdated) {
                        existingFacility.getTranslations().add(currentFacility.getTranslations().get(0));
                    }
                    facilityRepository.save(existingFacility);
                    updatedFacilityList.add(existingFacility);
                }
                else
                    updatedFacilityList.add(facilityRepository.save(currentFacility));
            }

            List<Facility> existingFacilityOtherLanguage = retrievedFacilities.stream()
                    .filter(retrievedFacility -> retrievedFacility.getTranslations().stream()
                            .noneMatch(facilityTranslation -> languageCode.equals(facilityTranslation.getLanguage())))
                    .collect(Collectors.toList());

            updatedFacilityList.addAll(existingFacilityOtherLanguage);
        }
        return updatedFacilityList;
    }

    // The updatePhoto method assume
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

        List<Photo> updatedPhotoList = new ArrayList<>();

        if (Objects.nonNull(photos) && !photos.isEmpty()) {
            for (Photo currentPhoto : photos) {
                Optional<Photo> retrievedCurrentPhotoOptional = retrievedPhotos.stream()
                        .filter(t -> t.getUrl().equals(currentPhoto.getUrl())
                                && t.getHdUrl().equals(currentPhoto.getHdUrl()))
                        .findFirst();
                if (retrievedCurrentPhotoOptional.isPresent())
                    updatedPhotoList.add(photoValues(currentPhoto, retrievedCurrentPhotoOptional.get()));
                else
                    updatedPhotoList.add(photoRepository.save(currentPhoto));
            }
        }
        return updatedPhotoList;
    }

    private List<Policy> updatePolicies(Hotel hotel, String languageCode) {

        List<Policy> hotelPolicies = hotel.getPolicies();
        List<Policy> retrievedPolicies = policyRepository.findAll();
        List<Policy> updatedPolicyList = new ArrayList<>();

        if (Objects.nonNull(hotelPolicies) && !hotelPolicies.isEmpty()) {
            for (Policy hotelCurrentPolicy : hotelPolicies) {
                Optional<Policy> retrievedCurrentPolicyOptional = retrievedPolicies.stream()
                        .filter(t -> t.equals(hotelCurrentPolicy))
                        .findFirst();
                if (retrievedCurrentPolicyOptional.isPresent())
                    updatedPolicyList.add(retrievedCurrentPolicyOptional.get());
                else
                    updatedPolicyList.add(policyRepository.save(hotelCurrentPolicy));
            }
            return updatedPolicyList;
        }
        return retrievedPolicies;
    }
}