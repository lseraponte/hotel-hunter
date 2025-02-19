package com.lseraponte.cupidapi.hh.model;

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

import java.util.List;

@Entity
@Table(name = "bed_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_type_id")
    private int bedTypeId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

    @Column(name = "quantity")
    private int quantity;

    @OneToMany(mappedBy = "bedTypeObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BedTypeTranslation> translations;

    // Convert from DTO to Entity
    public static BedType fromDTO(RoomDTO.BedTypeDTO dto) {
        return BedType.builder()
                .quantity(dto.quantity())
                .translations(List.of(BedTypeTranslation.builder()
                        .bedType(dto.bedType())
                        .bedSize(dto.bedSize())
                        .build()))
                .build();
    }
}
