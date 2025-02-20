package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.PhotoDTO;
import com.lseraponte.cupidapi.hh.dto.RoomDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "description")
    private String description;

    @Column(name = "room_size_square")
    private int roomSizeSquare;

    @Column(name = "room_size_unit")
    private String roomSizeUnit;

    @Column(name = "max_adults")
    private int maxAdults;

    @Column(name = "max_children")
    private int maxChildren;

    @Column(name = "max_occupancy")
    private int maxOccupancy;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BedType> bedTypes;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Amenity> roomAmenities;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    // Convert from DTO to Entity
    public static Room fromDTO(RoomDTO dto, Hotel hotel, String language) {
        Room room = Room.builder()
                .roomName(dto.roomName())
                .description(dto.description())
                .roomSizeSquare(dto.roomSizeSquare())
                .roomSizeUnit(dto.roomSizeUnit())
                .maxAdults(dto.maxAdults())
                .maxChildren(dto.maxChildren())
                .maxOccupancy(dto.maxOccupancy())
                .hotel(hotel)// Convert PhotoDTO to Photo
                .build();

        room.setBedTypes(Optional.ofNullable(dto.bedTypes()).orElse(Collections.emptyList()).stream()
                .map(bedTypeDTO -> BedType.fromDTO(bedTypeDTO, room, language)).collect(Collectors.toList()));
        room.setRoomAmenities(Optional.ofNullable(dto.roomAmenities()).orElse(Collections.emptyList()).stream()
                .map(amenityDTO -> Amenity.fromDTO(amenityDTO, room, language)).collect(Collectors.toList()));
        room.setPhotos(Optional.ofNullable(dto.photos()).orElse(Collections.emptyList()).stream()
                .map(photoDTO -> Photo.fromDTO(photoDTO, hotel)).collect(Collectors.toList()));

        return room;
    }

}

