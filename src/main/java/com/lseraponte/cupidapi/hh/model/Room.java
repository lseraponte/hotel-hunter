package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.PhotoDTO;
import com.lseraponte.cupidapi.hh.dto.RoomDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import java.util.List;
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

    @ElementCollection
    @CollectionTable(name = "room_bed_types", joinColumns = @JoinColumn(name = "room_id"))
    private List<BedType> bedTypes;

    @ElementCollection
    @CollectionTable(name = "room_amenities", joinColumns = @JoinColumn(name = "room_id"))
    private List<Amenity> roomAmenities;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    // Convert from DTO to Entity
    public static Room fromDTO(RoomDTO dto, Hotel hotel) {
        return Room.builder()
                .roomName(dto.roomName())
                .description(dto.description())
                .roomSizeSquare(dto.roomSizeSquare())
                .roomSizeUnit(dto.roomSizeUnit())
                .maxAdults(dto.maxAdults())
                .maxChildren(dto.maxChildren())
                .maxOccupancy(dto.maxOccupancy())
                .hotel(hotel)
                .bedTypes(convertToBedTypeEntities(dto.bedTypes()))  // Convert BedTypeDTO to BedType
                .roomAmenities(convertToAmenityEntities(dto.roomAmenities()))  // Convert AmenityDTO to Amenity
                .photos(convertToPhotoEntities(dto.photos(), hotel))  // Convert PhotoDTO to Photo
                .build();
    }

    // Helper method to convert BedTypeDTO to BedType
    private static List<BedType> convertToBedTypeEntities(List<RoomDTO.BedTypeDTO> bedTypeDTOs) {
        return bedTypeDTOs.stream()
                .map(BedType::fromDTO)  // Using BedType's fromDTO method
                .collect(Collectors.toList());
    }

    // Helper method to convert AmenityDTO to Amenity
    private static List<Amenity> convertToAmenityEntities(List<RoomDTO.AmenityDTO> amenityDTOs) {
        return amenityDTOs.stream()
                .map(Amenity::fromDTO)  // Using Amenity's fromDTO method
                .collect(Collectors.toList());
    }

    // Helper method to convert PhotoDTO to Photo, passing the hotel for each photo
    private static List<Photo> convertToPhotoEntities(List<PhotoDTO> photoDTOs, Hotel hotel) {
        return photoDTOs.stream()
                .map(photoDTO -> Photo.fromDTO(photoDTO, hotel))  // Using Photo's fromDTO method
                .collect(Collectors.toList());
    }
}

