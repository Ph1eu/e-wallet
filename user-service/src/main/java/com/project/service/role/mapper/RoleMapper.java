package com.project.service.role.mapper;

import com.project.service.role.dto.ERole;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    @Named("roleStringToERole")
    default ERole roleStringToERole(String role){
        if (role == null) {
            return null;
        }

        return ERole.valueOf(role);
    };
    @Named("eRoleToRoleString")
    default String eRoleToRoleString(ERole role){
        if (role == null) {
            return null;
        }

        return role.name();
    };

}
