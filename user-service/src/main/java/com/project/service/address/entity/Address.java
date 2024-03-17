package com.project.service.address.entity;

import com.project.service.address.dto.AddressDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Entity
@Table(name = "address")
@ToString
@EqualsAndHashCode
public class Address {
    @Id
    @Column(name = "address_id")
    String id;
    @Column(name = "street_address")
    @Setter
    String street_address;
    @Column(name = "city")
    @Setter
    String city;
    @Column(name = "province")
    @Setter
    String province;
    @Column(name = "country")
    @Setter
    String country;
    public Address(String street_address, String city, String province, String country) {
        this.street_address = street_address;
        this.city = city;
        this.province = province;
        this.country = country;
    }
    public Address(AddressDto addressDTO) {
        this.id = addressDTO.getId();
        this.street_address = addressDTO.getStreet_address();
        this.city = addressDTO.getCity();
        this.province = addressDTO.getProvince();
        this.country = addressDTO.getCountry();
    }

    public Address() {

    }
}
