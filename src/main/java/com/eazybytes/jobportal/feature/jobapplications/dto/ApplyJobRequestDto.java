package com.eazybytes.jobportal.feature.jobapplications.dto;

import jakarta.validation.constraints.NotNull;

public record ApplyJobRequestDto(
        @NotNull
        Long jobId,
        String coverLetter
) {
}
