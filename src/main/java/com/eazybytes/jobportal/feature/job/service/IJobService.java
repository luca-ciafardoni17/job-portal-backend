package com.eazybytes.jobportal.feature.job.service;

import com.eazybytes.jobportal.feature.job.dto.JobDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IJobService {
    List<JobDto> getEmployerJobs(String employerEmail);
    JobDto createJob(@Valid JobDto jobDto, String employerEmail);
    JobDto updateJobStatus(Long jobId, String status, String employerEmail);
}
