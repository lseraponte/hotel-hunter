package com.lseraponte.cupidapi.hh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record ReviewDTO(
        Integer averageScore,
        String country,
        String type,
        String name,
        String date,
        String headline,
        String language,
        String pros,
        String cons
) { }
