package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.AddressDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    // Method to convert from DTO to Entity
    public static Address fromDTO(AddressDTO dto) {
        return Address.builder()
                .address(dto.address())
                .city(dto.city())
                .country(dto.country())
                .build();
    }
}
