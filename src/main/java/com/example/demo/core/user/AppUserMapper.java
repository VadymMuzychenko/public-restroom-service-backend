package com.example.demo.core.user;

import com.example.demo.core.user.model.AppUser;
import com.example.demo.core.user.model.AppUserResponse;

public class AppUserMapper {

    public static AppUserResponse toAppUserResponse(AppUser appUser) {
        AppUserResponse appUserResponse = new AppUserResponse();
        appUserResponse.setId(appUser.getId());
        appUserResponse.setUsername(appUser.getUsername());
        appUserResponse.setFirstName(appUser.getFirstName());
        appUserResponse.setLastName(appUser.getLastName());
        return appUserResponse;
    }
}
