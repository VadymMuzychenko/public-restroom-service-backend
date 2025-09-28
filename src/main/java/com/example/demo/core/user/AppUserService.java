package com.example.demo.core.user;

import com.example.demo.core.PagingList;
import com.example.demo.core.user.model.AppUser;
import com.example.demo.core.user.model.AppUserResponse;
import com.example.demo.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserResponse getAppUser(UUID userId) {
        Optional<AppUser> user = appUserRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        AppUser appUser = user.get();
        return AppUserMapper.toAppUserResponse(appUser);
    }

    public PagingList<AppUserResponse> getAllAppUsers(int pageNumber, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<AppUser> page = appUserRepository.findAll(pageable);
        List<AppUserResponse> appUserResponses = page.stream().map(AppUserMapper::toAppUserResponse).toList();
        return new PagingList<>(pageNumber, page.getTotalPages(), page.getTotalElements(), appUserResponses);
    }
}
