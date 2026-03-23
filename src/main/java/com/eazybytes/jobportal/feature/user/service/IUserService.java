package com.eazybytes.jobportal.feature.user.service;

import com.eazybytes.jobportal.feature.user.dto.UserDto;

import java.util.Optional;

public interface IUserService {
    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);
    UserDto assignCompanyToEmployer(Long userId, Long companyId);
}
