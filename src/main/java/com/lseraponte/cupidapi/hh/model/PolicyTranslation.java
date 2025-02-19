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
@Table(name = "policy_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyTranslation {

    @EmbeddedId
    private PolicyTranslationId id;

    @ManyToOne
    @MapsId("policyId")
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "policy_type")
    private String policyType;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "child_allowed")
    private String childAllowed;

    @Column(name = "pets_allowed")
    private String petsAllowed;

    @Column(name = "parking")
    private String parking;
}