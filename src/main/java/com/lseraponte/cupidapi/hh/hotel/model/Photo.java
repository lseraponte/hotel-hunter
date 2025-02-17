package com.lseraponte.cupidapi.hh.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private int photoId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "url")
    private String url;

    @Column(name = "hd_url")
    private String hdUrl;

    @Column(name = "image_description")
    private String imageDescription;

    @Column(name = "image_class1")
    private String imageClass1;

    @Column(name = "image_class2")
    private String imageClass2;

    @Column(name = "main_photo")
    private boolean mainPhoto;

    @Column(name = "score")
    private double score;

    @Column(name = "class_id")
    private int classId;

    @Column(name = "class_order")
    private int classOrder;

    // Getters and Setters
}
