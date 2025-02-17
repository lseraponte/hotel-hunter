package com.lseraponte.cupidapi.hh.hotel;

import java.util.List;

public record Room(
        int id,
        String roomName,
        String description,
        int roomSizeSquare,
        String roomSizeUnit,
        String hotelId,
        int maxAdults,
        int maxChildren,
        int maxOccupancy,
        List<BedType> bedTypes,
        List<Amenity> roomAmenities,
        List<Photo> photos
) {
    public record BedType(int quantity, String bedType, String bedSize) {}

    public record Amenity(int amenitiesId, String name, int sort) {}
}
