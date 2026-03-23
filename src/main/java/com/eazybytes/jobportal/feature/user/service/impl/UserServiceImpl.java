package com.eazybytes.jobportal.feature.user.service.impl;

import com.eazybytes.jobportal.config.security.util.ApplicationUtil;
import com.eazybytes.jobportal.constants.AppConstants;
import com.eazybytes.jobportal.feature.user.dto.UserDto;
import com.eazybytes.jobportal.feature.company.entity.Company;
import com.eazybytes.jobportal.feature.user.entity.JobPortalUser;
import com.eazybytes.jobportal.feature.user.entity.Role;
import com.eazybytes.jobportal.feature.company.repository.CompanyRepository;
import com.eazybytes.jobportal.feature.user.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.feature.user.repository.RoleRepository;
import com.eazybytes.jobportal.feature.user.service.IUserService;
import com.eazybytes.jobportal.util.TransformationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    @Cacheable(value = "users", key = "#email")
    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return jobPortalUserRepository.findByEmail(email).map(TransformationUtil::transformUserToUserDto);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public UserDto elevateToEmployer(Long userId) {
        JobPortalUser user = jobPortalUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)
        );
        if (AppConstants.ROLE_EMPLOYER.equals(user.getRole().getName())) {
            throw new RuntimeException("User is already employer role");
        }
        if (AppConstants.ROLE_ADMIN.equals(user.getRole().getName())) {
            throw new RuntimeException("Cannot elevate admin user to employer role");
        }
        Role employerRole = roleRepository.findByName(AppConstants.ROLE_EMPLOYER)
                .orElseThrow(() -> new RuntimeException("ROLE_EMPLOYER not found"));
        user.setRole(employerRole);
        return TransformationUtil.transformUserToUserDto(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {
        JobPortalUser user = jobPortalUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        if (!AppConstants.ROLE_EMPLOYER.equals(user.getRole().getName())) {
            throw new RuntimeException("User must be an employer to be assigned to a company");
        }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        user.setCompany(company);
        return TransformationUtil.transformUserToUserDto(user);
    }

}
