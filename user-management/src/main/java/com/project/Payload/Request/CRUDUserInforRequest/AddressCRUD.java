package com.project.Payload.Request.CRUDUserInforRequest;

public class AddressCRUD {
    String id_email;
    String street_address;
    String city;
    String province;
    String country;

    public AddressCRUD(String id_email, String street_address, String city, String province, String country) {
        this.id_email = id_email;
        this.street_address = street_address;
        this.city = city;
        this.province = province;
        this.country = country;
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

    public String getId_email() {
        return id_email;
    }

    public void setId_email(String id_email) {
        this.id_email = id_email;
    }
}
