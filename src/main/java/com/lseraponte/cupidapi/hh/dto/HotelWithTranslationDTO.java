package com.lseraponte.cupidapi.hh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record HotelWithTranslationDTO(
        Integer hotelId,
        Integer cupidId,
        String hotelName,
        Integer stars,
        String description,
        String markdownDescription,
        String importantInfo,
        AddressDTO address,
        String phone,
        String fax,
        String email,
        Double rating,
        Integer reviewCount,
        String checkinStart,
        String checkinEnd,
        String checkout,
        String parking,
        Boolean childAllowed,
        Boolean petsAllowed,
        String mainImageTh
) {
}
