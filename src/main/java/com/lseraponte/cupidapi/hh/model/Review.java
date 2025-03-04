package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lseraponte.cupidapi.hh.dto.ReviewDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnore
    private Integer id;

    @Column(name = "average_score")
    private Integer averageScore;

    @Column(name = "country")
    private String country;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private String date;

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
    public static Review fromDTO(ReviewDTO dto) {
        return Review.builder()
                .averageScore(dto.averageScore())
                .country(dto.country())
                .type(dto.type())
                .name(dto.name())
                .date(dto.date())
                .headline(dto.headline())
                .language(dto.language())
                .pros(dto.pros())
                .cons(dto.cons())
                .build();
    }
}
