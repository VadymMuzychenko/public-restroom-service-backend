package com.example.demo.core.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponse {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
}
