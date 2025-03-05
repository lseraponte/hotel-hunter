package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lseraponte.cupidapi.hh.dto.FacilityDTO;
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
@Table(name = "facility_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_translation_id")
    @JsonIgnore
    private Integer id;

    @Column(name = "facility_name")
    private String facilityName;

    @Column(name = "language", nullable = false)
    @JsonIgnore
    private String language;

    public static FacilityTranslation fromDTO(FacilityDTO dto, String language) {

        return FacilityTranslation.builder()
                .facilityName(dto.name())
                .language(language)
                .build();
    }
}
