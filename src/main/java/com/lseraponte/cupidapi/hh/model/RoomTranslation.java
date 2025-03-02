package com.lseraponte.cupidapi.hh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lseraponte.cupidapi.hh.dto.RoomDTO;
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
@Table(name = "room_translations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_translation_id")
    @JsonIgnore
    private Integer id;

    @Column(name = "room_name")
    private String roomName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "language", nullable = false)
    @JsonIgnore
    private String language;

    public static RoomTranslation fromDTO(RoomDTO dto, String language) {

        return RoomTranslation.builder()
                .roomName(dto.roomName())
                .description(dto.description())
                .language(language)
                .build();

    }
}
