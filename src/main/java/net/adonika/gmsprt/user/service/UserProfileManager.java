package net.adonika.gmsprt.user.service;

import net.adonika.gmsprt.user.model.UserProfileAdd;
import net.adonika.gmsprt.user.model.UserProfileModify;
import net.adonika.gmsprt.user.model.UserProfileDetails;

import java.util.List;

public interface UserProfileManager {
    
    UserProfileDetails addUserProfile(UserProfileAdd userProfileAdd);
    
    UserProfileDetails modifyUserProfile(Long seqUserProfile, UserProfileModify userProfileModify);
    
    void removeUserProfile(Long seqUserProfile);

    List<UserProfileDetails> findUserProfile(Long seqUser);

}
