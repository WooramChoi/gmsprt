package net.adonika.gmsprt.security.model;

import net.adonika.gmsprt.user.model.UserProfileDetails;
import net.adonika.gmsprt.user.model.UserDetails;

import java.util.HashMap;
import java.util.Map;

public class SecurityDetails {

    private final Long seqUser;
    private final String name;
    private final String email;
    private final String urlPicture;

    private final Map<String, SecurityProfileDetails> profiles;

    public SecurityDetails(UserDetails userDetails) {
        this.seqUser = userDetails.getSeqUser();
        this.name = userDetails.getName();
        this.email = userDetails.getEmail();
        this.urlPicture = userDetails.getUrlPicture();

        this.profiles = new HashMap<>();
    }

    private static class SecurityProfileDetails {
        private final Long seqUserProfile;
        private final String sid;
        private final String uid;
        private final String name;
        private final String email;
        private final String urlPicture;

        public SecurityProfileDetails(UserProfileDetails userProfileDetails) {

            this.seqUserProfile = userProfileDetails.getSeqUserProfile();
            this.sid = userProfileDetails.getSid();
            this.uid = userProfileDetails.getUid();
            this.name = userProfileDetails.getName();
            this.email = userProfileDetails.getEmail();
            this.urlPicture = userProfileDetails.getUrlPicture();
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

    public Map<String, SecurityProfileDetails> getProfiles() {
        return profiles;
    }

    public void putProfile(UserProfileDetails userProfileDetails) {
        this.profiles.put(userProfileDetails.getProvider(), new SecurityProfileDetails(userProfileDetails));
    }
}
