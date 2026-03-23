package com.eazybytes.jobportal.feature.jobapplications.dto;

import com.eazybytes.jobportal.feature.job.dto.JobDto;
import com.eazybytes.jobportal.feature.profile.dto.ProfileDto;

import java.time.Instant;

public record JobApplicationDto(
        Long id,
        Long userId,
        String userName,
        String userEmail,
        String userMobileNumber,
        ProfileDto userProfile,
        JobDto job,
        Instant appliedAt,
        String status,
        String coverLetter,
        String notes
) {
}
