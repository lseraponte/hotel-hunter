package com.lseraponte.cupidapi.hh.model;

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
    private Integer id;

    @Column(name = "bed_type")
    private String bedTypeName;

    @Column(name = "bed_size")
    private String bedSize;

    @Column(name = "language", nullable = false)
    private String language;
}
