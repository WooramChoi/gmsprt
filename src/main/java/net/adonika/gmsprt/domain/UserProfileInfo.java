package net.adonika.gmsprt.domain;

import javax.persistence.*;

@Entity
@Table(
        name = "USER_PROFILE_INFO",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"PROVIDER", "SID"})
        }
)
public class UserProfileInfo extends CommInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ_USER_PROFILE", length = 10)
    private Long seqUserProfile;

    @Column(name = "PROVIDER", length = 10)
    private String provider;

    @Column(name = "SID", length = 30)
    private String sid;

    @Column(length = 30)
    private String uid;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String email;

    @Column(length = 100)
    private String urlPicture;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserInfo.class)
    @JoinColumn(name = "SEQ_USER")
    private UserInfo userInfo;

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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
