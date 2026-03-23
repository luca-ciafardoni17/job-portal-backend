package com.eazybytes.jobportal.feature.profile.repository;

import com.eazybytes.jobportal.feature.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}