package com.eazybytes.jobportal.feature.profile.service;

import com.eazybytes.jobportal.feature.profile.dto.ProfileDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;

public interface IProfileService {
    ProfileDto createOrUpdateProfile(String userEmail, String profileJson, MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException;
    ProfileDto getProfile(String userEmail);
    ProfileDto getProfileWithBinaryData(String userEmail);
}
