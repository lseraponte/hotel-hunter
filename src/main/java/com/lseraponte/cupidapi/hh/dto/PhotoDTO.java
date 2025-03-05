package com.lseraponte.cupidapi.hh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record PhotoDTO(
        String url,
        String hdUrl,
        String imageDescription,
        String imageClass1,
        String imageClass2,
        Boolean mainPhoto,
        Double score,
        Integer classId,
        Integer classOrder
) { }
