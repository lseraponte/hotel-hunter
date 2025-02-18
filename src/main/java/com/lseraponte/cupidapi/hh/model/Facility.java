package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.FacilityDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private int facilityId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "name")
    private String name;

    // Convert from DTO to Entity
    public static Facility fromDTO(FacilityDTO dto, Hotel hotel) {
        return Facility.builder()
                .facilityId(dto.facilityId()) // Assuming the DTO has an ID, otherwise remove
                .name(dto.name())
                .hotel(hotel)
                .build();
    }
}
