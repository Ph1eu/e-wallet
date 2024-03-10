package com.project.service_impl.role;

import com.project.service.role.dto.ERole;
import com.project.service.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    Role findRoleByName(String name);

    boolean existsByName(ERole name);
}

