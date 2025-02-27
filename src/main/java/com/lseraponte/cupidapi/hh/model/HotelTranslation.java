package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.HotelDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_translation_id")
    private Integer id;

    @Column(name = "hotel_type")
    private String hotelType;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "chain")
    private String chain;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "markdown_description")
    private String markdownDescription;

    @Lob
    @Column(name = "important_info")
    private String importantInfo;

    @Column(name = "language", nullable = false)
    private String language;

    public static HotelTranslation fromDTO(HotelDTO dto, String language) {

        return HotelTranslation.builder()
                .hotelType(dto.hotelType())
                .hotelName(dto.hotelName())
                .chain(dto.chain())
                .description(dto.description())
                .markdownDescription(dto.markdownDescription())
                .importantInfo(dto.importantInfo())
                .language(language)
                .build();
    }
}
