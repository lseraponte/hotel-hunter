package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.FacilityDTO;
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
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {

    @Id
    @Column(name = "facility_id")
    private Integer facilityId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private List<FacilityTranslation> translations;

    // Convert from DTO to Entity
    public static Facility fromDTO(FacilityDTO dto, String language) {

        Facility facility = Facility.builder()
                .facilityId(dto.facilityId())
                .build();

        FacilityTranslation translation = FacilityTranslation.builder()
                .facilityName(dto.name())
                .language(language)
                .build();

        facility.setTranslations(List.of(translation));

        return facility;
    }

}
