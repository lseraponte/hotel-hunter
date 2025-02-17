package com.lseraponte.cupidapi.hh.hotel.dto;

public record PolicyDTO(
        String policyType,
        String name,
        String description,
        String childAllowed,
        String petsAllowed,
        String parking
) { }
