package com.project.Repository;

import com.project.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Boolean existsByUsername(String username);
    Boolean existsByIdemail(String email);
    Optional<User> findByUsername(String username);

    @Override
    Optional<User> findById(String s);
}
