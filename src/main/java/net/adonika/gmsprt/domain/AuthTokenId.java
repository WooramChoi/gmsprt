package net.adonika.gmsprt.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.MappedSuperclass;

/**
 * AuthTokenInfo 의 IdClass 를 위한 class
 * 그러나 해당 Entity 바깥에서 일반 VO 처럼 사용되고 있음, 문제의 소지 있음
 */
@MappedSuperclass
public class AuthTokenId implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 5153748712262372278L;
    
    private String registrationId;
    private String accessToken;
    
    public AuthTokenId() {
        
    }
    
    public AuthTokenId(String registrationId, String accessToken) {
        this.registrationId = registrationId;
        this.accessToken = accessToken;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accessToken, registrationId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AuthTokenId other = (AuthTokenId) obj;
        return Objects.equals(accessToken, other.accessToken) && Objects.equals(registrationId, other.registrationId);
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
