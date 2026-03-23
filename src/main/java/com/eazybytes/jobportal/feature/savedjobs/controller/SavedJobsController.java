package com.eazybytes.jobportal.feature.savedjobs.controller;

import com.eazybytes.jobportal.feature.job.dto.JobDto;
import com.eazybytes.jobportal.feature.savedjobs.service.ISavedJobsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class SavedJobsController {

    private final ISavedJobsService savedJobsService;

    @PostMapping(value = "/saved-jobs/{jobId}/jobseeker")
    public ResponseEntity<JobDto> saveJob(@PathVariable Long jobId, Authentication authentication) {
        String userEmail = authentication.getName();
        JobDto savedJob = savedJobsService.saveJob(userEmail, jobId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
    }

    @DeleteMapping(value = "/saved-jobs/{jobId}/jobseeker")
    public ResponseEntity<String> unsaveJob(@PathVariable Long jobId, Authentication authentication) {
        String userEmail = authentication.getName();
        savedJobsService.unsaveJob(userEmail, jobId);
        return ResponseEntity.status(HttpStatus.OK).body("Job unsaved successfully");
    }

    @GetMapping(value = "/saved-jobs/jobseeker")
    public ResponseEntity<List<JobDto>> getSavedJobs(Authentication authentication) {
        String userEmail = authentication.getName();
        List<JobDto> savedJobDtos = savedJobsService.getSavedJobs(userEmail);
        return ResponseEntity.ok(savedJobDtos);
    }
}