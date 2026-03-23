package com.eazybytes.jobportal.auditor.aspects;

import com.eazybytes.jobportal.feature.auth.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class BeforeLoginSuccessAuditAspect {

    @AfterReturning(
            pointcut = "execution(* com.eazybytes.jobportal.feature.auth.controller.AuthController.apiLogin(..))",
            returning = "response"
    )
    public void logSuccessfulLogin(JoinPoint joinPoint, Object response) {
        if (!(response instanceof ResponseEntity<?> responseEntity)) {
            return;
        }
        Object body = responseEntity.getBody();
        if (!(body instanceof LoginResponseDto loginResponse)) {
            return;
        }
        if (loginResponse.user() != null) {
            String username = loginResponse.user().getEmail();
            String role = loginResponse.user().getRole();
            log.info("✅ LOGIN SUCCESS | User: {} | Role: {}", username, role);
        }
    }

}