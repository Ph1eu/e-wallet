package com.project.repository;
import java.util.Optional;

import com.project.model.ERole;
import com.project.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    Role findRoleByName(String name);
    boolean existsByName(ERole name);
}

