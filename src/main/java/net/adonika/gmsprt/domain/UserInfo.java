package net.adonika.gmsprt.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserInfo extends CommInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ_USER", length = 10)
    private Long seqUser;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String email;

    @Column(length = 100)
    private String urlPicture;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = UserProfileInfo.class, mappedBy = "userInfo")
    private List<UserProfileInfo> userProfileInfos = new ArrayList<>();

    public Long getSeqUser() {
        return seqUser;
    }

    public void setSeqUser(Long seqUser) {
        this.seqUser = seqUser;
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

    public void setUrlPicture(String urlProfile) {
        this.urlPicture = urlProfile;
    }

    public List<UserProfileInfo> getUserProfileInfos() {
        return userProfileInfos;
    }

    public void setUserProfileInfos(List<UserProfileInfo> userProfileInfos) {
        this.userProfileInfos = userProfileInfos;
    }
}
