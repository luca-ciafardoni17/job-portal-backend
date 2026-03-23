package com.eazybytes.jobportal.feature.auth.dto;

import com.eazybytes.jobportal.feature.user.dto.UserDto;

public record LoginResponseDto(
        String message,
        UserDto user,
        String jwtToken
) {
}
