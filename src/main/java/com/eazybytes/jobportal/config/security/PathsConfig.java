package com.eazybytes.jobportal.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {

    @Bean(name = "publicPaths")
    public List<String> publicPaths() {
        return List.of(
                "/api/contacts/public",
                "/api/auth/login/public",
                "/api/companies/public",
                "/api/auth/register/public",
                "/api/csrf-token/public",
                "/api/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of(
                "/api/**"
        );
    }

    @Bean(name = "adminPaths")
    public List<String> adminPaths() {
        return List.of(
                // ----- CONTACTS ----- //
                "/api/contacts/admin",
                "/api/contacts/sort/admin",
                "/api/contacts/page/admin",
                // ----- COMPANIES ----- //
                "/api/companies/admin",
                "/api/companies/${id}/admin",
                // ----- USERS ----- //
                "/api/users/search/admin",
                "/api/users/${userId}/role/employer/admin",
                "/api/users/${userId}/company/${companyId}/admin"
        );
    }

    @Bean(name = "employerPaths")
    public List<String> employerPaths() {
        return List.of(
                // ----- JOBS ----- //
                "/api/jobs/employer",
                "/api/jobs/${jobId}/status/employer"
        );
    }

    @Bean(name = "jobSeekerPaths")
    public List<String> jobSeekerPaths() {
        return List.of(
                // ----- PROFILE ----- //
                "/api/users/profile/jobseeker",
                "/api/users/profile/resume/jobseeker",
                "/api/users/profile/picture/jobseeker",
                // ----- SAVED JOBS ----- //
                "/api/users/saved-jobs/jobseeker",
                "/api/users/saved-jobs/${jobId}/jobseeker",
                // ----- JOB APPLICATIONS ----- //
                "/api/users/job-applications/jobseeker",
                "/api/users/job-applications/${jobId}/jobseeker"
        );
    }

}
