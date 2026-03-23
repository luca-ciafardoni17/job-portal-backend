package com.eazybytes.jobportal.feature.jobapplications.controller;

import com.eazybytes.jobportal.feature.jobapplications.dto.ApplyJobRequestDto;
import com.eazybytes.jobportal.feature.jobapplications.dto.JobApplicationDto;
import com.eazybytes.jobportal.feature.jobapplications.service.IJobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class JobApplicationController {

    private final IJobApplicationService jobApplicationService;

    @PostMapping(value = "/job-applications/jobseeker")
    public ResponseEntity<JobApplicationDto> applyForJob(
            @RequestBody @Valid ApplyJobRequestDto applyJobRequestDto,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        JobApplicationDto application = jobApplicationService.applyForJob(userEmail, applyJobRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @DeleteMapping(value = "/job-applications/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<String> withdrawApplication(
            @PathVariable Long jobId,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        jobApplicationService.withdrawApplication(userEmail, jobId);
        return ResponseEntity.status(HttpStatus.OK).body("Application withdrawn successfully");
    }

    @GetMapping(value = "/job-applications/jobseeker", version = "1.0")
    public ResponseEntity<List<JobApplicationDto>> getJobSeekerApplications(Authentication authentication) {
        String userEmail = authentication.getName();
        List<JobApplicationDto> applications = jobApplicationService.getJobSeekerApplications(userEmail);
        return ResponseEntity.ok(applications);
    }
}
