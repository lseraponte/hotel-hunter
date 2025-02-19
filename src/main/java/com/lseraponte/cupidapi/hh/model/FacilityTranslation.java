package com.lseraponte.cupidapi.hh.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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

    @EmbeddedId
    private FacilityTranslationId id;

    @ManyToOne
    @MapsId("facilityId")
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Column(name = "facility_name")
    private String facilityName;
}
