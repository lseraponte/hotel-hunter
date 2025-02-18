package com.lseraponte.cupidapi.hh.dto;

import java.util.List;

public record RoomDTO(
        int id,
        String roomName,
        String description,
        int roomSizeSquare,
        String roomSizeUnit,
        String hotelId,
        int maxAdults,
        int maxChildren,
        int maxOccupancy,
        List<BedTypeDTO> bedTypes,
        List<AmenityDTO> roomAmenities,
        List<PhotoDTO> photos
) {
    public record BedTypeDTO(int quantity, String bedType, String bedSize) {}

    public record AmenityDTO(int amenitiesId, String name, int sort) {}
}
