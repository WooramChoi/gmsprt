package net.adonika.gmsprt.security.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import net.adonika.gmsprt.security.model.AuthTokenAuthentication;
import net.adonika.gmsprt.user.model.UserProfileDetails;

@Component
public class AuthTokenAuthenticationProvider implements AuthenticationProvider {
    
    private final AuthTokenManager authTokenManager;
    
    public AuthTokenAuthenticationProvider(AuthTokenManager authTokenManager) {
        this.authTokenManager = authTokenManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        AuthTokenAuthentication authTokenAuthentication = (AuthTokenAuthentication) authentication;
        
        UserProfileDetails principal = authTokenManager.findUserProfile(authTokenAuthentication.getDetails()).orElse(null);
        authTokenAuthentication.setPrincipal(principal);
        
        return authTokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthTokenAuthentication.class.equals(authentication);
    }
    
    

}
