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
@Entity
@Table(name = "address")
@ToString
@EqualsAndHashCode
public class Address {
    @Id
    @Getter
    @Column(name = "address_id")
    String id;
    @Column(name = "street_address")
    @Getter
    @Setter
    String Street_address;
    @Column(name = "city")
    @Getter
    @Setter
    String City;
    @Column(name = "province")
    @Getter
    @Setter
    String province;
    @Column(name = "country")
    @Getter
    @Setter
    String country;
    public Address(String street_address, String city, String province, String country) {
        this.Street_address = street_address;
        this.City = city;
        this.province = province;
        this.country = country;
    }
    public Address(AddressDto addressDTO) {
        this.id = addressDTO.getId();
        this.Street_address = addressDTO.getStreet_address();
        this.City = addressDTO.getCity();
        this.province = addressDTO.getProvince();
        this.country = addressDTO.getCountry();
    }

    public Address() {

    }
}
