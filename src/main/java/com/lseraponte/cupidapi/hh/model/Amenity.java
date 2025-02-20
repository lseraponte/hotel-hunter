package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.RoomDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Table(name = "amenities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amenity {

    @Id
    @Column(name = "amenity_id")
    private int amenityId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @Column(name = "sort")
    private int sort;

    @OneToMany(mappedBy = "amenity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AmenityTranslation> translations;

    // Convert from DTO to Entity
    public static Amenity fromDTO(RoomDTO.AmenityDTO dto, Room room, String language) {

        Amenity amenity = Amenity.builder()
                .amenityId(dto.amenitiesId())
                .room(room)
                .sort(dto.sort())
                .build();

        AmenityTranslation translation = AmenityTranslation.builder()
                .amenity(amenity)
                .name(dto.name())
                .language(language)
                .build();

        amenity.setTranslations(List.of(translation));

        return amenity;
    }
}

