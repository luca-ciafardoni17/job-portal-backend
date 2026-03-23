package com.eazybytes.jobportal.feature.contact.dto;

import java.time.Instant;

public record ContactResponseDto(
        Long id,
        String name,
        String email,
        String userType,
        String subject,
        String message,
        String status,
        Instant createdAt
) {
}
