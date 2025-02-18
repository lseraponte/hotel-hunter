package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private int hotelId;

    @Column(name = "cupid_id")
    private int cupidId;

    @Column(name = "main_image_th")
    private String mainImageTh;

    @Column(name = "hotel_type")
    private String hotelType;

    @Column(name = "hotel_type_id")
    private int hotelTypeId;

    @Column(name = "chain")
    private String chain;

    @Column(name = "chain_id")
    private int chainId;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "hotel_name")
    private String hotelName;

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

    @Column(name = "description")
    private String description;

    @Column(name = "markdown_description")
    private String markdownDescription;

    @Column(name = "important_info")
    private String importantInfo;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Facility> facilities;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Policy> policies;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;

    // Method to convert from DTO to Entity
    public static Hotel fromDTO(HotelDTO dto) {
        Hotel hotel = Hotel.builder()
                .hotelId(dto.hotelId())
                .cupidId(dto.cupidId())
                .mainImageTh(dto.mainImageTh())
                .hotelType(dto.hotelType())
                .hotelTypeId(dto.hotelTypeId())
                .chain(dto.chain())
                .chainId(dto.chainId())
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .hotelName(dto.hotelName())
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
                .description(dto.description())
                .markdownDescription(dto.markdownDescription())
                .importantInfo(dto.importantInfo())
                .build();

        hotel.setPhotos(dto.photos().stream().map(photoDTO -> Photo.fromDTO(photoDTO, hotel)).collect(Collectors.toList()));
        hotel.setFacilities(dto.facilities().stream().map(facilityDTO -> Facility.fromDTO(facilityDTO, hotel)).collect(Collectors.toList()));
        hotel.setPolicies(dto.policies().stream().map(policyDTO -> Policy.fromDTO(policyDTO, hotel)).collect(Collectors.toList()));
        hotel.setRooms(dto.rooms().stream().map(roomDTO -> Room.fromDTO(roomDTO, hotel)).collect(Collectors.toList()));

        return hotel;
    }
}
