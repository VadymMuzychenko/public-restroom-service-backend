package com.example.demo;

import com.example.demo.core.role.Role;
import com.example.demo.core.role.RoleNames;
import com.example.demo.core.role.RoleRepository;
import com.example.demo.core.user.AppUserRepository;
import com.example.demo.core.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FirstStartLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(FirstStartLoader.class);

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (roleRepository.findByName(RoleNames.ADMIN).isEmpty()) {
            Role role = new Role();
            role.setName(RoleNames.ADMIN);
            roleRepository.save(role);
        }
        if (roleRepository.findByName(RoleNames.MODERATOR).isEmpty()) {
            Role role = new Role();
            role.setName(RoleNames.MODERATOR);
            roleRepository.save(role);
        }
        if (roleRepository.findByName(RoleNames.USER).isEmpty()) {
            Role role = new Role();
            role.setName(RoleNames.USER);
            roleRepository.save(role);
        }

        if (appUserRepository.countByRoles_Name(RoleNames.ADMIN) == 0) {
            String baseUsername = "admin";
            String candidateUsername = baseUsername;
            int counter = 1;

            while (appUserRepository.getUserByUsername(candidateUsername) != null) {
                candidateUsername = baseUsername + counter;
                counter++;
            }

            AppUser admin = new AppUser();
            admin.setUsername(candidateUsername);
            admin.setPasswordHash(passwordEncoder.encode("password"));
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("admin@email.com");
            admin.setRoles(Set.of(roleRepository.findByName(RoleNames.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"))));
            admin.setCreatedAt(OffsetDateTime.now());
            appUserRepository.save(admin);
            logger.warn("Administrator not found. New administrator created: " + candidateUsername);
        }
    }
}
