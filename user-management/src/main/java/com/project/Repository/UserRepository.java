package com.project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.Model.*;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Boolean existsByUsername(String username);
    Boolean existsByIdemail(String email);
    Optional<User> findByUsername(String username);

    }
