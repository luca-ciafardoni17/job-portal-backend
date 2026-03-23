package com.eazybytes.jobportal.auditor;

import com.eazybytes.jobportal.config.security.util.ApplicationUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(ApplicationUtil.getLoggedInUser());
    }

}
