package net.adonika.gmsprt.security.service;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import net.adonika.gmsprt.util.SecurityUtil;

@Component
public class AuthTokenUserAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityUtil.getCurrentSeqUser());
    }
}
