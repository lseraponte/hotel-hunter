package com.lseraponte.cupidapi.hh.dto;

public record PhotoDTO(
        String url,
        String hdUrl,
        String imageDescription,
        String imageClass1,
        String imageClass2,
        boolean mainPhoto,
        double score,
        int classId,
        int classOrder
) { }
