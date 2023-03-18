package net.adonika.gmsprt.security.model;

import net.adonika.gmsprt.user.model.UserProfileVO;
import net.adonika.gmsprt.user.model.UserVO;

import java.util.HashMap;
import java.util.Map;

public class SecurityVO {

    private final Long seqUser;
    private final String name;
    private final String email;
    private final String urlPicture;

    private final Map<String, SecurityProfileVO> profiles;

    public SecurityVO(UserVO userVO) {
        this.seqUser = userVO.getSeqUser();
        this.name = userVO.getName();
        this.email = userVO.getEmail();
        this.urlPicture = userVO.getUrlPicture();

        this.profiles = new HashMap<>();
    }

    private static class SecurityProfileVO {
        private final Long seqUserProfile;
        private final String sid;
        private final String uid;
        private final String name;
        private final String email;
        private final String urlPicture;

        public SecurityProfileVO(UserProfileVO userProfileVO) {

            this.seqUserProfile = userProfileVO.getSeqUserProfile();
            this.sid = userProfileVO.getSid();
            this.uid = userProfileVO.getUid();
            this.name = userProfileVO.getName();
            this.email = userProfileVO.getEmail();
            this.urlPicture = userProfileVO.getUrlPicture();
        }

        public Long getSeqUserProfile() {
            return seqUserProfile;
        }
        public String getSid() {
            return sid;
        }
        public String getUid() {
            return uid;
        }
        public String getName() {
            return name;
        }
        public String getEmail() {
            return email;
        }
        public String getUrlPicture() {
            return urlPicture;
        }
    }

    public Long getSeqUser() {
        return seqUser;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public Map<String, SecurityProfileVO> getProfiles() {
        return profiles;
    }

    public void putProfile(UserProfileVO userProfileVO) {
        this.profiles.put(userProfileVO.getProvider(), new SecurityProfileVO(userProfileVO));
    }
}
