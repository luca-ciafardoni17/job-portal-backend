package com.eazybytes.jobportal.feature.auth.controller;

import com.eazybytes.jobportal.config.security.util.JwtUtil;
import com.eazybytes.jobportal.constants.AppConstants;
import com.eazybytes.jobportal.feature.auth.dto.LoginRequestDto;
import com.eazybytes.jobportal.feature.auth.dto.LoginResponseDto;
import com.eazybytes.jobportal.feature.auth.dto.RegisterRequestDto;
import com.eazybytes.jobportal.feature.user.dto.UserDto;
import com.eazybytes.jobportal.feature.user.entity.JobPortalUser;
import com.eazybytes.jobportal.feature.user.repository.JobPortalUserRepository;
import com.eazybytes.jobportal.feature.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    public final JobPortalUserRepository jobPortalUserRepository;
    public final RoleRepository roleRepository;

    @PostMapping( "/login/public")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            var authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDto.username(),
                    loginRequestDto.password()
            ));
            String jwtToken = jwtUtil.generateJwtToken(authResult);
            var userDto = new UserDto();
            var loggedInUser = (JobPortalUser) authResult.getPrincipal();
            BeanUtils.copyProperties(loggedInUser, userDto);
            userDto.setRole(loggedInUser.getRole().getName());
            userDto.setUserId(loggedInUser.getId());
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseDto(
                    HttpStatus.OK.getReasonPhrase(),
                    userDto,
                    jwtToken
            ));
        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    @PostMapping("/register/public")
    public ResponseEntity<?> apiRegister(@RequestBody RegisterRequestDto registerRequestDto) {
        // RegisterValidationAspect done @Before
        JobPortalUser jobPortalUser = new JobPortalUser();
        BeanUtils.copyProperties(registerRequestDto, jobPortalUser);
        jobPortalUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        jobPortalUser.setRole(roleRepository.findByName(AppConstants.ROLE_JOB_SEEKER).orElseThrow(() ->
                new IllegalArgumentException("Role not found")));
        jobPortalUserRepository.save(jobPortalUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new LoginResponseDto(message, null, null));
    }

}
