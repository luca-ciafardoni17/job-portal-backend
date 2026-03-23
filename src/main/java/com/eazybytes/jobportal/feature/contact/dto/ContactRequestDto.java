package com.eazybytes.jobportal.feature.contact.dto;

import com.eazybytes.jobportal.feature.contact.entity.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link Contact}
 */
public record ContactRequestDto(

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email")
        String email,

        @NotBlank(message = "Message cannot be empty")
        @Size(min = 5, max = 500, message = "Message length must be between 5 and 500 characters")
        String message,

        @NotBlank(message = "Name cannot be empty")
        @Size(min = 5, max = 30, message = "Name length must be between 5 and 30 characters")
        String name,

        @NotBlank(message = "Subject cannot be empty")
        @Size(min = 5, max = 150, message = "Subject length must be between 5 and 150 characters")
        String subject,

        @NotBlank(message = "User type cannot be empty")
        @Pattern(regexp = "Job Seeker|Employer|Other", message = "Invalid user type (allowed: Job Seeker, Employer, Other)")
        String userType

) implements Serializable {
}