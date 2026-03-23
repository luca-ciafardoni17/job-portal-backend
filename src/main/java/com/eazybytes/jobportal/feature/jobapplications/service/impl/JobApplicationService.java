package com.eazybytes.jobportal.feature.jobapplications.service.impl;

import com.eazybytes.jobportal.constants.AppConstants;
import com.eazybytes.jobportal.feature.job.entity.Job;
import com.eazybytes.jobportal.feature.job.repository.JobRepository;
import com.eazybytes.jobportal.feature.jobapplications.dto.ApplyJobRequestDto;
import com.eazybytes.jobportal.feature.jobapplications.dto.JobApplicationDto;
import com.eazybytes.jobportal.feature.jobapplications.entity.JobApplication;
import com.eazybytes.jobportal.feature.jobapplications.repository.JobApplicationRepository;
import com.eazybytes.jobportal.feature.jobapplications.service.IJobApplicationService;
import com.eazybytes.jobportal.feature.user.entity.JobPortalUser;
import com.eazybytes.jobportal.feature.user.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.util.TransformationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobApplicationService implements IJobApplicationService {

    private final JobRepository jobRepository;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Override
    @Transactional
    public JobApplicationDto applyForJob(String userEmail, ApplyJobRequestDto applyJobRequestDto) {
        JobPortalUser user = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        Long jobId = applyJobRequestDto.jobId();
        if (jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new RuntimeException("You have already applied for this job");
        }
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        JobApplication application = new JobApplication();
        application.setUser(user);
        application.setJob(job);
        application.setAppliedAt(Instant.now());
        application.setStatus(AppConstants.PENDING_STATUS);
        application.setCoverLetter(applyJobRequestDto.coverLetter());
        JobApplication saved = jobApplicationRepository.save(application);
        job.setApplicationsCount(job.getApplicationsCount() != null ? job.getApplicationsCount() + 1 : 1);
        return TransformationUtil.mapToJobApplicationDto(saved);
    }

    @Override
    @Transactional
    public void withdrawApplication(String userEmail, Long jobId) {
        JobPortalUser user = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        if (!jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new RuntimeException("You have not applied for this job");
        }
        jobApplicationRepository.deleteByUserIdAndJobId(user.getId(), jobId);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));
        if (job.getApplicationsCount() != null && job.getApplicationsCount() > 0) {
            job.setApplicationsCount(job.getApplicationsCount() - 1);
        }
    }

    @Override
    public List<JobApplicationDto> getJobSeekerApplications(String userEmail) {
        JobPortalUser user = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        return user.getJobApplications().stream().map(
                TransformationUtil::mapToJobApplicationDto
                ).collect(Collectors.toList());
    }
}
