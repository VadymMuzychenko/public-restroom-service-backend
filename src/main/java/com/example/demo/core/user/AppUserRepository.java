package com.example.demo.core.user;

import com.example.demo.core.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {
    boolean existsByUsername(String username);

    AppUser getUserByUsername(String username);

    boolean existsByEmail(String email);

    long countByRoles_Name(String name);
}
