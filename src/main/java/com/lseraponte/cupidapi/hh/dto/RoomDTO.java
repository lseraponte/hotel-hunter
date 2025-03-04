package com.lseraponte.cupidapi.hh.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record RoomDTO(
        Integer id,
        String roomName,
        String description,
        Integer roomSizeSquare,
        String roomSizeUnit,
        String hotelId,
        Integer maxAdults,
        Integer maxChildren,
        Integer maxOccupancy,
        List<BedTypeDTO> bedTypes,
        List<AmenityDTO> roomAmenities,
        List<PhotoDTO> photos
) {

}
