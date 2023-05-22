package com.user_management.Repository;
import java.util.Optional;

import com.user_management.Model.ERole;
import com.user_management.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}

