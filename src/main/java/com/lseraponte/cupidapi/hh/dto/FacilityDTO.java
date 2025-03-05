package com.lseraponte.cupidapi.hh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record FacilityDTO(
        Integer facilityId,
        String name
) { }
