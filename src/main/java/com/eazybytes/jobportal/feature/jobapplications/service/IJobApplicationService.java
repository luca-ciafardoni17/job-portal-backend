package com.eazybytes.jobportal.feature.jobapplications.service;

import com.eazybytes.jobportal.feature.jobapplications.dto.ApplyJobRequestDto;
import com.eazybytes.jobportal.feature.jobapplications.dto.JobApplicationDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IJobApplicationService {
    JobApplicationDto applyForJob(String userEmail, @Valid ApplyJobRequestDto applyJobRequestDto);
    void withdrawApplication(String userEmail, Long jobId);
    List<JobApplicationDto> getJobSeekerApplications(String userEmail);
}
