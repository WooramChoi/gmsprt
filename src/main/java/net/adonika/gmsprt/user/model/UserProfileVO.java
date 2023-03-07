package net.adonika.gmsprt.user.model;

import java.util.Objects;

import net.adonika.gmsprt.comm.model.CommVO;

public class UserProfileVO extends CommVO {
    private Long seqUserProfile;
    private String provider;
    private String sid;
    private String uid;
    private String name;
    private String email;
    private String urlPicture;
    
    private UserVO user;

    @Override
    public int hashCode() {
        return Objects.hash(seqUserProfile);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserProfileVO other = (UserProfileVO) obj;
        return Objects.equals(seqUserProfile, other.seqUserProfile);
    }

    public Long getSeqUserProfile() {
        return seqUserProfile;
    }

    public void setSeqUserProfile(Long seqUserProfile) {
        this.seqUserProfile = seqUserProfile;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
    
}
