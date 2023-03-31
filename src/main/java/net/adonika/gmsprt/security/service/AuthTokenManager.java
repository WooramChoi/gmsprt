package net.adonika.gmsprt.security.service;

import java.util.Optional;

import net.adonika.gmsprt.domain.AuthTokenId;
import net.adonika.gmsprt.user.model.UserProfileDetails;

public interface AuthTokenManager {
    
    Optional<UserProfileDetails> saveAuthToken(AuthTokenId authTokenId);
    
    Optional<UserProfileDetails> findUserProfile(AuthTokenId authTokenId);
    
    void removeAuthToken(AuthTokenId authTokenId);

}
