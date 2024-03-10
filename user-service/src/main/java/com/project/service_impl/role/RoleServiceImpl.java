package com.project.service_impl.role;

import com.project.service.role.RoleService;
import com.project.service.role.dto.ERole;
import com.project.service.role.entity.Role;
import com.project.service.role.dto.RoleDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    RoleRepository roleRepository;
    @Override
    public RoleDTO findbyName(ERole erole) {

        Role role = roleRepository.findByName(erole).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        logger.info("User's role found");
        return new RoleDTO(role);
    }
    @Override
    public void addRole(RoleDTO roleDTO) {
        Role role = new Role(roleDTO);
        roleRepository.save(role);
    }
    @Override
    public boolean checkExistRole(RoleDTO roleDTO) {
        return roleRepository.existsByName(roleDTO.getName());
    }
}
