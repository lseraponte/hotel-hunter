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
@Table(name = "bed_type_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedTypeTranslation {

    @EmbeddedId
    private BedTypeTranslationId id;

    @ManyToOne
    @MapsId("bedTypeId")
    @JoinColumn(name = "bed_type_id", nullable = false)
    private BedType bedTypeObject;

    @Column(name = "bed_type")
    private String bedType;

    @Column(name = "bed_size")
    private String bedSize;
}
