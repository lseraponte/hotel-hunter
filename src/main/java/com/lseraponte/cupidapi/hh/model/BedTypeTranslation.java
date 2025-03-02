package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lseraponte.cupidapi.hh.dto.RoomDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "bed_type_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedTypeTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_type_translation_id")
    @JsonIgnore
    private Integer id;

    @Column(name = "bed_type")
    private String bedTypeName;

    @Column(name = "bed_size")
    private String bedSize;

    @Column(name = "language", nullable = false)
    @JsonIgnore
    private String language;

    public static BedTypeTranslation fromDTO(RoomDTO.BedTypeDTO dto, String language) {

        return BedTypeTranslation.builder()
                .bedTypeName(dto.bedType())
                .bedSize(dto.bedSize())
                .language(language)
                .build();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BedTypeTranslation that = (BedTypeTranslation) o;
        return Objects.equals(bedTypeName, that.bedTypeName) &&
                Objects.equals(bedSize, that.bedSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bedTypeName, bedSize);
    }
}
