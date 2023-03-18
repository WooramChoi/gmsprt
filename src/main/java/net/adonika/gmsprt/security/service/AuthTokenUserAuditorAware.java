package net.adonika.gmsprt.security.service;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.util.Optional;

@Component
public class AuthTokenUserAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long seqUser;
        try {
            seqUser = NumberUtils.parseNumber(authentication.getName(), Long.class);
        } catch(NullPointerException | NumberFormatException e) {
            // don't mind!
            seqUser = null;
        }
        return Optional.ofNullable(seqUser);
    }
}
