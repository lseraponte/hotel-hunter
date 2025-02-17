package com.lseraponte.cupidapi.hh.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Amenity {

    @Column(name = "amenities_id")
    private int amenitiesId;

    @Column(name = "name")
    private String name;

    @Column(name = "sort")
    private int sort;

    // Getters and Setters
}
