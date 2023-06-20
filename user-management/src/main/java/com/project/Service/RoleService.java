package com.project.Service;

import com.project.Model.ERole;
import com.project.Model.Role;
import com.project.Payload.DTO.RoleDTO;
import com.project.Repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleDTO findbyName(ERole erole){
        try {
            Role role = roleRepository.findByName(erole).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            logger.info("User's role found");
            return new RoleDTO(role);
        }
        catch (Exception e){
            logger.error("Failed to find role  from the database.", e);
            throw new RuntimeException("Failed to find role.", e);
        }
    }
}
