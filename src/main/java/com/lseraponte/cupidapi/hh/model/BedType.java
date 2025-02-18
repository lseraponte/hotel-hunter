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
public class BedType {

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "bed_type")
    private String bedType;

    @Column(name = "bed_size")
    private String bedSize;

    // Convert from DTO to Entity
    public static BedType fromDTO(RoomDTO.BedTypeDTO dto) {
        return BedType.builder()
                .quantity(dto.quantity())
                .bedType(dto.bedType())
                .bedSize(dto.bedSize())
                .build();
    }
}
