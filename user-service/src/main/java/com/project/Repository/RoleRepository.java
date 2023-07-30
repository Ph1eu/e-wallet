package com.project.Repository;
import java.util.Optional;

import com.project.Model.ERole;
import com.project.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
    Role findRoleByName(String name);
}

