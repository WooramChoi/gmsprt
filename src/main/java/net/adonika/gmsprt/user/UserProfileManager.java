package net.adonika.gmsprt.user;

import net.adonika.gmsprt.domain.UserProfileInfo;

import java.util.List;

public interface UserProfileManager {

    UserProfileInfo getUserProfile(Long seqUserProfile);

    UserProfileInfo getUserProfile(String provider, String sid);

    UserProfileInfo create(UserProfileInfo userProfileInfo, Long seqUser);

    UserProfileInfo update(UserProfileInfo userProfileInfo, List<String> ignores);

}
