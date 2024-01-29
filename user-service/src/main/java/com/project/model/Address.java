package com.project.model;

import com.project.payload.dto.AddressDTO;
import jakarta.persistence.*;

@Entity
@Table(name="address")
public class Address {
    @Id
    @Column(name="address_id")

    String id;
    @Column(name="street_address")
    String Street_address;
    @Column(name="city")
    String City;
    @Column(name="province")
    String province;
    @Column(name="country")
    String country;

    public Address(String id, String street_address, String city, String province, String country) {
        this.id = id;
        Street_address = street_address;
        City = city;
        this.province = province;
        this.country = country;
    }

    public Address(String street_address, String city, String province, String country) {
        this.Street_address = street_address;
        this.City = city;
        this.province = province;
        this.country = country;
    }
    public Address(AddressDTO addressDTO) {
        this.id = addressDTO.getId();
        this.Street_address = addressDTO.getStreet_address();
        this.City = addressDTO.getCity();
        this.province = addressDTO.getProvince();
        this.country = addressDTO.getCountry();
    }
    public Address() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet_address() {
        return Street_address;
    }

    public void setStreet_address(String street_address) {
        Street_address = street_address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
