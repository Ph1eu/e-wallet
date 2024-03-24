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
    @Column(name = "street")
    @Setter
    String street;
    @Column(name = "city")
    @Setter
    String city;
    @Column(name = "province")
    @Setter
    String province;
    @Column(name = "country")
    @Setter
    String country;
}
