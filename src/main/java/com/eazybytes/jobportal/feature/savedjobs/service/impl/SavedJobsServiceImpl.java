package com.eazybytes.jobportal.feature.savedjobs.service.impl;

import com.eazybytes.jobportal.feature.job.dto.JobDto;
import com.eazybytes.jobportal.feature.job.entity.Job;
import com.eazybytes.jobportal.feature.job.repository.JobRepository;
import com.eazybytes.jobportal.feature.savedjobs.service.ISavedJobsService;
import com.eazybytes.jobportal.feature.user.entity.JobPortalUser;
import com.eazybytes.jobportal.feature.user.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.util.TransformationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SavedJobsServiceImpl implements ISavedJobsService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final JobRepository jobRepository;

    @CacheEvict(value = "companies", key = "#userEmail")
    @Override
    @Transactional
    public JobDto saveJob(String userEmail, Long jobId) {
        JobPortalUser jobPortalUser = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Not found user with email: " + userEmail));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("No job found with id: " + jobId));
        jobPortalUser.getJobs().add(job);
        return TransformationUtil.transformJobToDto(job);
    }

    @CacheEvict(value = "companies", key = "#userEmail")
    @Override
    @Transactional
    public void unsaveJob(String userEmail, Long jobId) {
        JobPortalUser jobPortalUser = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Not found user with email: " + userEmail));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("No job found with id: " + jobId));
        jobPortalUser.getJobs().remove(job);
    }

    @Cacheable(value = "saved-jobs", key = "#userEmail")
    @Override
    public List<JobDto> getSavedJobs(String userEmail) {
        JobPortalUser jobPortalUser = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("Not found user with email: " + userEmail));
        return jobPortalUser.getJobs().stream().map(TransformationUtil::transformJobToJobDto).collect(Collectors.toList());
    }

}
