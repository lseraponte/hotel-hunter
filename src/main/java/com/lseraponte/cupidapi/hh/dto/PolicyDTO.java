package com.lseraponte.cupidapi.hh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record PolicyDTO(
        String policyType,
        String name,
        String description,
        String childAllowed,
        String petsAllowed,
        String parking
) { }
