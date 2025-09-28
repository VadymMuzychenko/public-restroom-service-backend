package com.example.demo.controllers;

import com.example.demo.core.PagingList;
import com.example.demo.core.user.AppUserService;
import com.example.demo.core.user.model.AppUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/{userId}")
    public ResponseEntity<AppUserResponse> getAppUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(appUserService.getAppUser(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public PagingList<AppUserResponse> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return appUserService.getAllAppUsers(pageNumber, pageSize);
    }
}
