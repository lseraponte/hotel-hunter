package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.PhotoDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "photos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private int photoId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;

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

    // Convert from DTO to Entity
    public static Photo fromDTO(PhotoDTO dto, Hotel hotel) {
        return Photo.builder()
                .url(dto.url())
                .hdUrl(dto.hdUrl())
                .imageDescription(dto.imageDescription())
                .imageClass1(dto.imageClass1())
                .imageClass2(dto.imageClass2())
                .mainPhoto(dto.mainPhoto())
                .score(dto.score())
                .classId(dto.classId())
                .classOrder(dto.classOrder())
                .hotel(hotel)
                .build();
    }
}
