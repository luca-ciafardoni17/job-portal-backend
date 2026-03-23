package com.eazybytes.jobportal.feature.savedjobs.service;

import com.eazybytes.jobportal.feature.job.dto.JobDto;

import java.util.List;

public interface ISavedJobsService {
    JobDto saveJob(String userEmail, Long jobId);
    void unsaveJob(String userEmail, Long jobId);
    List<JobDto> getSavedJobs(String userEmail);
}
