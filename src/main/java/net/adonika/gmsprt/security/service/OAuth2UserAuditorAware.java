package net.adonika.gmsprt.security.service;

import net.adonika.gmsprt.security.model.OAuth2UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OAuth2UserAuditorAware implements AuditorAware<Long> {

    private final OAuth2UserManager oAuth2UserManager;

    public OAuth2UserAuditorAware (OAuth2UserManager oAuth2UserManager) {
        this.oAuth2UserManager = oAuth2UserManager;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {

        Long seqUser;
        OAuth2UserPrincipal oAuth2UserPrincipal = oAuth2UserManager.getOAuth2UserPrincipal();
        if (oAuth2UserPrincipal != null) {
            seqUser = oAuth2UserPrincipal.getSeqUser();
        } else {
            seqUser = null;
        }

        return Optional.ofNullable(seqUser);
    }
}
