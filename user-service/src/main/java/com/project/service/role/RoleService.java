package com.project.service.role;

import com.project.service.role.dto.ERole;
import com.project.service.role.dto.RoleDto;

public interface RoleService {
    RoleDto findbyName(ERole erole);
    void addRole(RoleDto roleDTO);
    boolean checkExistRole(RoleDto roleDTO);
}
