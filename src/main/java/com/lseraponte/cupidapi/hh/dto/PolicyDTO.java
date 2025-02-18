package com.lseraponte.cupidapi.hh.dto;

public record PolicyDTO(
        String policyType,
        String name,
        String description,
        String childAllowed,
        String petsAllowed,
        String parking
) { }
