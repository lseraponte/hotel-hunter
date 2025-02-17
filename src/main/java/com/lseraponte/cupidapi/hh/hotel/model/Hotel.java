package com.lseraponte.cupidapi.hh.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private int hotelId;

    @Column(name = "cupid_id")
    private int cupidId;

    @Column(name = "main_image_th")
    private String mainImageTh;

    @Column(name = "hotel_type")
    private String hotelType;

    @Column(name = "hotel_type_id")
    private int hotelTypeId;

    @Column(name = "chain")
    private String chain;

    @Column(name = "chain_id")
    private int chainId;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "fax")
    private String fax;

    @Column(name = "email")
    private String email;

    @Embedded
    private Address address;

    @Column(name = "stars")
    private int stars;

    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "rating")
    private double rating;

    @Column(name = "review_count")
    private int reviewCount;

    @Column(name = "checkin_start")
    private String checkinStart;

    @Column(name = "checkin_end")
    private String checkinEnd;

    @Column(name = "checkout")
    private String checkout;

    @Column(name = "parking")
    private String parking;

    @Column(name = "group_room_min")
    private int groupRoomMin;

    @Column(name = "child_allowed")
    private boolean childAllowed;

    @Column(name = "pets_allowed")
    private boolean petsAllowed;

    @Column(name = "description")
    private String description;

    @Column(name = "markdown_description")
    private String markdownDescription;

    @Column(name = "important_info")
    private String importantInfo;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Facility> facilities;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Policy> policies;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;

    // Getters and Setters
}
