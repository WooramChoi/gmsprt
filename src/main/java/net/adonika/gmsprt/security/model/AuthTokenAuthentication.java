package net.adonika.gmsprt.security.model;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import net.adonika.gmsprt.domain.AuthTokenId;
import net.adonika.gmsprt.user.model.UserProfileDetails;

public class AuthTokenAuthentication implements Authentication {

    /**
     * 
     */
    private static final long serialVersionUID = 5471207839429476288L;
    
    private final AuthTokenId details;
    private UserProfileDetails principal;
    
    public AuthTokenAuthentication(String registrationId, String accessToken) {
        details = new AuthTokenId(registrationId, accessToken);
        principal = null;
    }

    /*
     * ID
     */
    @Override
    public String getName() {
        String name;
        if (principal != null
                && principal.getUser() != null
                && principal.getUser().getSeqUser() != null) {
            name = String.valueOf(principal.getUser().getSeqUser());
        } else {
            name = null;
        }
        return name;
    }

    /*
     * ROLE_
     * ROLE_ADMIN / ROLE_MANAGER / ROLE_EMPLOYEE / ROLE_USER 로 구상중이지만,
     * 당장에 USER 만 사용하기에 구현하지 않음
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * PASSWORD
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /*
     * ID, PASSWORD 외에 인증정보를 확인할 수 있는 정보
     */
    @Override
    public AuthTokenId getDetails() {
        return details;
    }

    /*
     * 사용자 상세 정보
     */
    @Override
    public UserProfileDetails getPrincipal() {
        return principal;
    }
    
    public void setPrincipal(UserProfileDetails userProfileDetails) throws IllegalArgumentException {
        if (principal != null) {
            throw new IllegalArgumentException("principal already exists");
        } else {
            principal = userProfileDetails;
        }
    }

    /*
     * Manager 혹은 Provider 를 통해 인증이 확인된경우 true 를 반환
     * (최초 생성시 true 로 고정시키는게 안정적이라고 함)
     */
    @Override
    public boolean isAuthenticated() {
        return (principal != null);
    }

    /*
     * 왠만하면, isAuthenticated = false 로 변경시킬때만 호출하는게 좋다고 함
     * 적합하지 않은 경로를 통해 true 로 변경시키려 한 경우, throw IllegalArgumentException
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("param 'isAuthenticated' can not be true");
        } else {
            principal = null;
        }
    }

}
