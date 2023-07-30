package com.project.Payload.DTO;


import com.project.Model.Address;

import java.util.Objects;

public class AddressDTO {

    String id;

    String Street_address;

    String City;

    String province;
    String country;

    public AddressDTO(String id, String street_address, String city, String province, String country) {
        this.id = id;
        Street_address = street_address;
        City = city;
        this.province = province;
        this.country = country;
    }

    public AddressDTO(String street_address, String city, String province, String country) {
        this.Street_address = street_address;
        this.City = city;
        this.province = province;
        this.country = country;
    }
    public AddressDTO(Address address) {
        this.id = address.getId();
        this.Street_address = address.getStreet_address();
        this.City = address.getCity();
        this.province = address.getProvince();
        this.country = address.getCountry();
    }
    public AddressDTO() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressDTO that = (AddressDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(Street_address, that.Street_address) && Objects.equals(City, that.City) && Objects.equals(province, that.province) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Street_address, City, province, country);
    }
}
