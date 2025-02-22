package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.PolicyDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    private int policyId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private List<PolicyTranslation> translations;

    // Convert from DTO to Entity
    public static Policy fromDTO(PolicyDTO dto, Hotel hotel, String language) {

        Policy policy = Policy.builder()
                .build();

        PolicyTranslation translation = PolicyTranslation.builder()
                .policyType(dto.policyType())
                .name(dto.name())
                .description(dto.description())
                .childAllowed(dto.childAllowed())
                .petsAllowed(dto.petsAllowed())
                .parking(dto.parking())
                .language(language)
                .build();

        policy.setTranslations(List.of(translation));

        return policy;
    }

}
