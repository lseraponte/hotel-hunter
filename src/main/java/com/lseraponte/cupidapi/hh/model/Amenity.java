package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.AmenityDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "amenities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amenity {

    @Id
    @Column(name = "amenity_id")
    private Integer amenityId;

    @Column(name = "sort")
    private Integer sort;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id")
    private List<AmenityTranslation> translations;

    public static Amenity fromDTO(AmenityDTO dto, String language) {

        Amenity amenity = Amenity.builder()
                .amenityId(dto.amenitiesId())
                .sort(dto.sort())
                .build();

        AmenityTranslation translation = AmenityTranslation.builder()
                .name(dto.name())
                .language(language)
                .build();

        amenity.setTranslations(List.of(translation));

        return amenity;
    }
}
