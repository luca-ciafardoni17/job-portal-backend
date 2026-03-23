package com.eazybytes.jobportal.feature.auth.dto;

public record LoginRequestDto(
        String username,
        String password
) {
}
