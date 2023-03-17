package net.adonika.gmsprt.security.service;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class OAuth2UserAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {

        Long seqUser = null;
        
        // TODO get seqUser

        return Optional.ofNullable(seqUser);
    }
}
