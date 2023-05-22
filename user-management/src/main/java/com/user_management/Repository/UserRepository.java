package com.user_management.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.user_management.Model.*;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public interface UserRepository extends JpaRepository<User,String> {


    Optional<User> findByUsername(String username);
    }
