package com.lseraponte.cupidapi.hh.hotel.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "description")
    private String description;

    @Column(name = "room_size_square")
    private int roomSizeSquare;

    @Column(name = "room_size_unit")
    private String roomSizeUnit;

    @Column(name = "max_adults")
    private int maxAdults;

    @Column(name = "max_children")
    private int maxChildren;

    @Column(name = "max_occupancy")
    private int maxOccupancy;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BedType> bedTypes;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Amenity> roomAmenities;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    // Getters and Setters
}
