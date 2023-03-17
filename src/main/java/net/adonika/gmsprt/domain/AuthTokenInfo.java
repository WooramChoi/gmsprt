package net.adonika.gmsprt.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@IdClass(AuthTokenId.class)
@Entity
public class AuthTokenInfo extends CommInfo {
    
    @Id
    @Column(length = 10)
    private String registrationId;
    
    @Id
    @Column(length = 500)
    private String accessToken;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserProfileInfo.class)
    @JoinColumn(name = "SEQ_USER_PROFILE")
    private UserProfileInfo userProfileInfo;
    
    // TODO 이 접속에 관한 정보들(기기, IP 등등). 필요할까 싶지만.

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

    public UserProfileInfo getUserProfileInfo() {
        return userProfileInfo;
    }

    public void setUserProfileInfo(UserProfileInfo userProfileInfo) {
        this.userProfileInfo = userProfileInfo;
    }

}
