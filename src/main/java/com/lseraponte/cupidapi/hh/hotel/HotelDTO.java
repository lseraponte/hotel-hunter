package com.lseraponte.cupidapi.hh.hotel;


import java.util.List;

public record HotelDTO(
        int hotelId,
        int cupidId,
        String mainImageTh,
        String hotelType,
        int hotelTypeId,
        String chain,
        int chainId,
        double latitude,
        double longitude,
        String hotelName,
        String phone,
        String fax,
        String email,
        Address address,
        int stars,
        String airportCode,
        double rating,
        int reviewCount,
        String checkinStart,
        String checkinEnd,
        String checkout,
        String parking,
        int groupRoomMin,
        boolean childAllowed,
        boolean petsAllowed,
        String description,
        String markdownDescription,
        String importantInfo,
        List<Photo> photos,
        List<Facility> facilities,
        List<Policy> policies,
        List<Room> rooms
) { }
