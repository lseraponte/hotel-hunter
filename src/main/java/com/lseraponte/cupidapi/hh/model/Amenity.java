package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.RoomDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amenity {

    @Column(name = "amenities_id")
    private int amenitiesId;

    @Column(name = "name")
    private String name;

    @Column(name = "sort")
    private int sort;

    // Convert from DTO to Entity
    public static Amenity fromDTO(RoomDTO.AmenityDTO dto) {
        return Amenity.builder()
                .amenitiesId(dto.amenitiesId())
                .name(dto.name())
                .sort(dto.sort())
                .build();
    }
}
