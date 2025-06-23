package com.univille.fso.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.univille.fso.entity.UserRole;
import com.univille.fso.repository.UserRoleRepository;

@Component
public class Startup {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(userRoleRepository.findAll().isEmpty()) {
            UserRole userRole = new UserRole();
            userRole.setCode("ROLE_ADMIN");
            userRole.setName("Administrador");
            userRoleRepository.save(userRole);
            userRole = new UserRole();
            userRole.setCode("ROLE_USER");
            userRole.setName("Usuário");
            userRoleRepository.save(userRole);
        }
    }
}
