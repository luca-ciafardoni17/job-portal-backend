package com.eazybytes.jobportal.feature.profile.service.impl;

import com.eazybytes.jobportal.feature.job.dto.JobDto;
import com.eazybytes.jobportal.feature.profile.dto.ProfileDto;
import com.eazybytes.jobportal.feature.user.entity.JobPortalUser;
import com.eazybytes.jobportal.feature.profile.entity.Profile;
import com.eazybytes.jobportal.feature.user.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.feature.profile.repository.ProfileRepository;
import com.eazybytes.jobportal.feature.profile.service.IProfileService;
import com.eazybytes.jobportal.util.TransformationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements IProfileService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final ProfileRepository profileRepository;

    @CacheEvict(value = "profile", allEntries = true)
    @Override
    @Transactional
    public ProfileDto createOrUpdateProfile(
            String userEmail, String profileJson,
            MultipartFile profilePicture, MultipartFile resume
    ) throws JsonProcessingException {
        JobPortalUser user = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ProfileDto profileDto = objectMapper.readValue(profileJson, ProfileDto.class);
        Profile savedProfile = profileRepository.save(TransformationUtil.transformUserToProfile(profile, profileDto, profilePicture, resume));
        return TransformationUtil.transformProfileToProfileDto(savedProfile, false);
    }

    @Cacheable(value = "profile", key = "#userEmail + 'no-binary-data'")
    @Override
    public ProfileDto getProfile(String userEmail) {
        JobPortalUser user = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return TransformationUtil.transformProfileToProfileDto(user.getProfile(), false);
    }

    @Cacheable(value = "profile", key = "#userEmail + 'binary-data'")
    @Override
    public ProfileDto getProfileWithBinaryData(String userEmail) {
        JobPortalUser user = jobPortalUserRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));
        if (user.getProfile() == null) {
            return null;
        }
        return TransformationUtil.transformProfileToProfileDto(user.getProfile(), true);
    }

}
