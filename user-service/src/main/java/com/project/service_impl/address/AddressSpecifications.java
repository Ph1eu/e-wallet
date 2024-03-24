package com.project.service_impl.address;

import com.project.service.address.entity.Address;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecifications {
    public static Specification<Address> withStreet(String street) {
        return (root, query, cb) -> cb.equal(root.get("street"), street);
    }

    public static Specification<Address> withCity(String city) {
        return (root, query, cb) -> cb.equal(root.get("city"), city);
    }

    public static Specification<Address> withProvince(String province) {
        return (root, query, cb) -> cb.equal(root.get("province"), province);
    }

    public static Specification<Address> withCountry(String country) {
        return (root, query, cb) -> cb.equal(root.get("country"), country);
    }
}
