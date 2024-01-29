package com.project.payload.request.CRUDUserInforRequest;

public class AddressCRUD {
    String street_address;
    String city;
    String province;

    String country;

    public AddressCRUD( String street_address, String city, String province, String country) {
        this.street_address = street_address;
        this.city = city;
        this.province = province;
        this.country = country;
    }

    public AddressCRUD() {
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
