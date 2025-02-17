package com.lseraponte.cupidapi.hh.hotel;

public record Policy(
        String policyType,
        String name,
        String description,
        String childAllowed,
        String petsAllowed,
        String parking
) { }
