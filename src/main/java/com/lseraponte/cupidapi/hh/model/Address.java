package com.lseraponte.cupidapi.hh.model;

import com.lseraponte.cupidapi.hh.dto.AddressDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Address {

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    public static Address fromDTO(AddressDTO dto) {
        return Address.builder()
                .address(dto.address())
                .city(dto.city())
                .country(dto.country())
                .build();
    }
}
