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
@Table(name = "hotel_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelTranslation {

    @EmbeddedId
    private HotelTranslationId id;

    @ManyToOne
    @MapsId("hotelId")
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "hotel_type")
    private String hotelType;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "chain")
    private String chain;

    @Column(name = "description")
    private String description;

    @Column(name = "markdown_description")
    private String markdownDescription;

    @Column(name = "important_info")
    private String importantInfo;
}
