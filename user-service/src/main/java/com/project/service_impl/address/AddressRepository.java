package com.project.service_impl.address;

import com.project.service.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressRepository extends JpaRepository<Address, String>, JpaSpecificationExecutor<Address> {
}
