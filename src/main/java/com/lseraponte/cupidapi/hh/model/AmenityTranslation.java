package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lseraponte.cupidapi.hh.dto.AmenityDTO;
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

@Entity
@Table(name = "amenity_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmenityTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "amenity_translation_id")
    @JsonIgnore
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "language", nullable = false)
    @JsonIgnore
    private String language;

    public static AmenityTranslation fromDTO(AmenityDTO dto, String language) {

        return AmenityTranslation.builder()
                .name(dto.name())
                .language(language)
                .build();
    }
}
