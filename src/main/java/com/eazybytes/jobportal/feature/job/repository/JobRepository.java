package com.eazybytes.jobportal.feature.job.repository;

import com.eazybytes.jobportal.feature.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
