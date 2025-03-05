package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lseraponte.cupidapi.hh.dto.BedTypeDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
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
    @JsonIgnore
    private Integer bedTypeId;

    @Column(name = "quantity")
    private Integer quantity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_type_id")
    private List<BedTypeTranslation> translations;

    // Convert from DTO to Entity
    public static BedType fromDTO(BedTypeDTO dto, String language) {

        BedType bedType = BedType.builder()
                .quantity(dto.quantity())
                .build();

        BedTypeTranslation translation = BedTypeTranslation.builder()
                .bedTypeName(dto.bedType())
                .bedSize(dto.bedSize())
                .language(language)
                .build();

        bedType.setTranslations(new ArrayList<>(List.of(translation)));

        return bedType;
    }

}
