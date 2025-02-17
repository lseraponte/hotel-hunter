package com.lseraponte.cupidapi.hh.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BedType {

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "bed_type")
    private String bedType;

    @Column(name = "bed_size")
    private String bedSize;

    // Getters and Setters
}
