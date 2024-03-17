package com.project.service.role.mapper;

import com.project.service.role.dto.RoleDto;
import com.project.service.role.entity.Role;
import org.mapstruct.factory.Mappers;

public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDto roleToRoleDto(Role role);
    Role roleDtoToRole(RoleDto roleDto);
}
