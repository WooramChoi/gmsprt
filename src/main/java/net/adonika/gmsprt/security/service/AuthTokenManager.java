package net.adonika.gmsprt.security.service;

import java.util.Optional;

import net.adonika.gmsprt.domain.AuthTokenId;
import net.adonika.gmsprt.user.model.UserProfileVO;

public interface AuthTokenManager {
    
    Optional<UserProfileVO> saveAuthToken(AuthTokenId authTokenId);
    
    Optional<UserProfileVO> findUserProfile(AuthTokenId authTokenId);
    
    void removeAuthToken(AuthTokenId authTokenId);

}
