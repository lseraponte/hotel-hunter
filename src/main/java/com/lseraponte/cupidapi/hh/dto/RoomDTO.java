package com.lseraponte.cupidapi.hh.dto;

import java.util.List;

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
    public record BedTypeDTO(Integer quantity, String bedType, String bedSize) {}

    public record AmenityDTO(Integer amenitiesId, String name, Integer sort) {}
}
