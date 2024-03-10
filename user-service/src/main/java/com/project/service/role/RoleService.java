package com.project.service.role;

import com.project.service.role.dto.ERole;
import com.project.service.role.dto.RoleDTO;

public interface RoleService {
    RoleDTO findbyName(ERole erole);
    void addRole(RoleDTO roleDTO);
    boolean checkExistRole(RoleDTO roleDTO);
}
