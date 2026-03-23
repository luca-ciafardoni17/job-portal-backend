package com.eazybytes.jobportal.feature.user.repository;

import com.eazybytes.jobportal.feature.user.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {

    Optional<JobPortalUser> findByEmailOrMobileNumber(String email, String mobileNumber);

    Optional<JobPortalUser> findByEmail(String email);

}