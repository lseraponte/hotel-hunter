package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.util.Objects;

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
    @JsonIgnore
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
    @JsonIgnore
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return Objects.equals(policyType, policy.policyType) &&
                Objects.equals(name, policy.name) &&
                Objects.equals(description, policy.description) &&
                Objects.equals(childAllowed, policy.childAllowed) &&
                Objects.equals(petsAllowed, policy.petsAllowed) &&
                Objects.equals(parking, policy.parking) &&
                Objects.equals(language, policy.language);
    }

}
