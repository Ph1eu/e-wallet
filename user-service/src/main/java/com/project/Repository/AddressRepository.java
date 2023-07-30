package com.project.Repository;

import com.project.Model.Address;
import com.project.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,String> {
}
