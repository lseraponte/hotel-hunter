package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.PolicyDTO;
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
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Integer policyId;

    @Column(name = "policy_type")
    private String policyType;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "child_allowed")
    private String childAllowed;

    @Column(name = "pets_allowed")
    private String petsAllowed;

    @Column(name = "parking")
    private String parking;

    @Column(name = "language", nullable = false)
    private String language;

    // Convert from DTO to Entity
    public static Policy fromDTO(PolicyDTO dto, String language) {

        return Policy.builder()
                .policyType(dto.policyType())
                .name(dto.name())
                .description(dto.description())
                .childAllowed(dto.childAllowed())
                .petsAllowed(dto.petsAllowed())
                .parking(dto.parking())
                .language(language)
                .build();

    }

}
