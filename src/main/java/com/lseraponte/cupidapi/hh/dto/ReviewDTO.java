package com.lseraponte.cupidapi.hh.dto;

public record ReviewDTO(
        Integer averageScore,
        String country,
        String type,
        String name,
        String date,
        String headline,
        String language,
        String pros,
        String cons
) { }
