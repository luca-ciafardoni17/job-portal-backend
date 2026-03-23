package com.eazybytes.jobportal.feature.job.service.impl;

import com.eazybytes.jobportal.feature.job.dto.JobDto;
import com.eazybytes.jobportal.feature.job.entity.Job;
import com.eazybytes.jobportal.feature.user.entity.JobPortalUser;
import com.eazybytes.jobportal.feature.job.repository.JobRepository;
import com.eazybytes.jobportal.feature.job.service.IJobService;
import com.eazybytes.jobportal.feature.user.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.util.TransformationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    public final JobPortalUserRepository jobPortalUserRepository;
    public final JobRepository jobRepository;

    @Cacheable(value = "jobs", key = "#employerEmail")
    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {
        JobPortalUser employer = jobPortalUserRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found!"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer company not found!");
        }
        List<Job> jobs = employer.getCompany().getJobs();
        return jobs.stream().map(TransformationUtil::transformJobToDto).collect(Collectors.toList());
    }

    @CacheEvict(value = "jobs", allEntries = true)
    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {
        JobPortalUser employer = jobPortalUserRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned. Please contact admin.");
        }
        Job job = TransformationUtil.transformJobDtoToJob(jobDto);
        job.setPostedDate(Instant.now());
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob = jobRepository.save(job);
        return TransformationUtil.transformJobToDto(savedJob);
    }

    @CacheEvict(value = "jobs", allEntries = true)
    @Override
    @Transactional
    public JobDto updateJobStatus(Long jobId, String status, String employerEmail) {
        if (!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT")) {
            throw new RuntimeException("Invalid status. Must be ACTIVE, CLOSED, or DRAFT");
        }
        JobPortalUser employer = jobPortalUserRepository.findByEmail(employerEmail)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        if (employer.getCompany() == null) {
            throw new RuntimeException("Employer does not have a company assigned");
        }
        Job job = employer.getCompany().getJobs().stream().filter(j -> j.getId().equals(jobId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job.setStatus(status);
        return TransformationUtil.transformJobToDto(job);
    }
}
