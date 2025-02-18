package com.lseraponte.cupidapi.hh.dto;

public record ReviewDTO(
        int averageScore,
        String country,
        String type,
        String name,
        String date,
        String headline,
        String language,
        String pros,
        String cons
) { }
