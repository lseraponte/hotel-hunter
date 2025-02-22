package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Hotel {

    @Id
    @Column(name = "hotel_id")
    private int hotelId;

    @Column(name = "cupid_id")
    private int cupidId;

    @Column(name = "main_image_th")
    private String mainImageTh;

    @Column(name = "hotel_type_id")
    private int hotelTypeId;

    @Column(name = "chain_id")
    private int chainId;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "email")
    private String email;

    @Embedded
    private Address address;

    @Column(name = "stars")
    private int stars;

    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "rating")
    private double rating;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "checkin_start")
    private String checkinStart;

    @Column(name = "checkin_end")
    private String checkinEnd;

    @Column(name = "checkout")
    private String checkout;

    @Column(name = "parking")
    private String parking;

    @Column(name = "group_room_min")
    private int groupRoomMin;

    @Column(name = "child_allowed")
    private boolean childAllowed;

    @Column(name = "pets_allowed")
    private boolean petsAllowed;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private List<Photo> photos;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private List<Facility> facilities;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private List<Policy> policies;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private List<Room> rooms;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private List<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private List<HotelTranslation> translations;

    // Method to convert from DTO to Entity
    public static Hotel fromDTO(HotelDTO dto, String language) {
        Hotel hotel = Hotel.builder()
                .hotelId(dto.hotelId())
                .cupidId(dto.cupidId())
                .mainImageTh(dto.mainImageTh())
                .hotelTypeId(dto.hotelTypeId())
                .chainId(dto.chainId())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .phone(dto.phone())
                .fax(dto.fax())
                .email(dto.email())
                .address(new Address(dto.address().address(), dto.address().city(), dto.address().country()))
                .stars(dto.stars())
                .airportCode(dto.airportCode())
                .rating(dto.rating())
                .reviewCount(dto.reviewCount())
                .checkinStart(dto.checkinStart())
                .checkinEnd(dto.checkinEnd())
                .checkout(dto.checkout())
                .parking(dto.parking())
                .groupRoomMin(dto.groupRoomMin())
                .childAllowed(dto.childAllowed())
                .petsAllowed(dto.petsAllowed())
                .build();

        HotelTranslation translation = HotelTranslation.builder()
                .language(language)
                .hotelType(dto.hotelType())
                .chain(dto.chain())
                .hotelName(dto.hotelName())
                .description(dto.description())
                .importantInfo(dto.importantInfo())
                .markdownDescription(dto.markdownDescription())
                .build();

        hotel.setTranslations(List.of(translation));

        hotel.setPhotos(Optional.ofNullable(dto.photos()).orElse(Collections.emptyList()).stream()
                .map(photoDTO -> Photo.fromDTO(photoDTO, hotel)).collect(Collectors.toList()));
        hotel.setFacilities(Optional.ofNullable(dto.facilities()).orElse(Collections.emptyList()).stream()
                .map(facilityDTO -> Facility.fromDTO(facilityDTO, hotel, language)).collect(Collectors.toList()));
        hotel.setPolicies(Optional.ofNullable(dto.policies()).orElse(Collections.emptyList()).stream()
                .map(policyDTO -> Policy.fromDTO(policyDTO, hotel, language)).collect(Collectors.toList()));
        hotel.setRooms(Optional.ofNullable(dto.rooms()).orElse(Collections.emptyList()).stream()
                .map(roomDTO -> Room.fromDTO(roomDTO, hotel, language)).collect(Collectors.toList()));
        hotel.setReviews(Optional.ofNullable(dto.reviews()).orElse(Collections.emptyList()).stream()
                .map(reviewDTO -> Review.fromDTO(reviewDTO, hotel)).collect(Collectors.toList()));

        return hotel;
    }

}
