package com.lseraponte.cupidapi.hh.dto;

public record HotelWithTranslationDTO(
        int hotelId,
        int cupidId,
        String hotelName,
        int stars,
        String description,
        String markdownDescription,
        String importantInfo,
        AddressDTO address,
        String phone,
        String fax,
        String email,
        double rating,
        int reviewCount,
        String checkinStart,
        String checkinEnd,
        String checkout,
        String parking,
        boolean childAllowed,
        boolean petsAllowed,
        String mainImageTh
) {
}
