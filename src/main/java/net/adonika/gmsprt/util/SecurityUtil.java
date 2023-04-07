package net.adonika.gmsprt.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.NumberUtils;

import net.adonika.gmsprt.security.model.AuthTokenAuthentication;

public class SecurityUtil {
    
    public static AuthTokenAuthentication getAuthentication() {
        
        AuthTokenAuthentication authTokenAuthentication;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication instanceof AuthTokenAuthentication) {
            authTokenAuthentication = (AuthTokenAuthentication) authentication;
        } else {
            authTokenAuthentication = null;
        }
        
        return authTokenAuthentication;
    }
    
    public static Long getCurrentSeqUser() {
        
        Long seqUser;
        AuthTokenAuthentication authentication = getAuthentication();
        
        if (authentication != null
                && authentication.getName() != null) {
            seqUser = NumberUtils.parseNumber(authentication.getName(), Long.class);
        } else {
            seqUser = null;
        }
        
        return seqUser;
    }

}
