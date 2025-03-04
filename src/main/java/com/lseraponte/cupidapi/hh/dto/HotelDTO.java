package com.lseraponte.cupidapi.hh.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record HotelDTO(
        Integer hotelId,
        Integer cupidId,
        String mainImageTh,
        String hotelType,
        Integer hotelTypeId,
        String chain,
        Integer chainId,
        Double latitude,
        Double longitude,
        String hotelName,
        String phone,
        String fax,
        String email,
        AddressDTO address,
        Integer stars,
        String airportCode,
        Double rating,
        Integer reviewCount,
        String checkinStart,
        String checkinEnd,
        String checkout,
        String parking,
        Integer groupRoomMin,
        Boolean childAllowed,
        Boolean petsAllowed,
        String description,
        String markdownDescription,
        String importantInfo,
        List<PhotoDTO> photos,
        List<FacilityDTO> facilities,
        List<PolicyDTO> policies,
        List<RoomDTO> rooms,
        List<ReviewDTO> reviews
) { }
