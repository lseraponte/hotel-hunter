package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "average_score")
    private int averageScore;

    @Column(name = "country")
    private String country;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "headline")
    private String headline;

    @Column(name = "language")
    private String language;

    @Lob
    @Column(name = "pros", columnDefinition = "TEXT")
    private String pros;

    @Lob
    @Column(name = "cons", columnDefinition = "TEXT")
    private String cons;

    // Utility Method to Convert DTO to Entity
    public static Review fromDTO(ReviewDTO dto, Hotel hotel) {
        return Review.builder()
                .hotel(hotel)
                .averageScore(dto.averageScore())
                .country(dto.country())
                .type(dto.type())
                .name(dto.name())
                .date(LocalDateTime.parse(dto.date(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .headline(dto.headline())
                .language(dto.language())
                .pros(dto.pros())
                .cons(dto.cons())
                .build();
    }
}
