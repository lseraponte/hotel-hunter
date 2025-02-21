package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "bed_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"room"})
public class BedType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_type_id")
    private int bedTypeId;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    @JsonBackReference
    private Room room;

    @Column(name = "quantity")
    private int quantity;

    @JsonManagedReference
    @OneToMany(mappedBy = "bedType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BedTypeTranslation> translations;

    // Convert from DTO to Entity
    public static BedType fromDTO(RoomDTO.BedTypeDTO dto, Room room, String language) {

        BedType bedType = BedType.builder()
                .quantity(dto.quantity())
                .room(room)
                .build();

        BedTypeTranslation translation = BedTypeTranslation.builder()
                .bedTypeName(dto.bedType())
                .bedSize(dto.bedSize())
                .bedType(bedType)
                .language(language)
                .build();

        bedType.setTranslations(List.of(translation));

        return bedType;
    }

}
