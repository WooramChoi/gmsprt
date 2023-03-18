package net.adonika.gmsprt.user.service;

import net.adonika.gmsprt.user.model.UserProfileAdd;
import net.adonika.gmsprt.user.model.UserProfileModify;
import net.adonika.gmsprt.user.model.UserProfileVO;

import java.util.List;

public interface UserProfileManager {
    
    UserProfileVO addUserProfile(UserProfileAdd userProfileAdd);
    
    UserProfileVO modifyUserProfile(Long seqUserProfile, UserProfileModify userProfileModify);
    
    void removeUserProfile(Long seqUserProfile);

    List<UserProfileVO> findUserProfile(Long seqUser);

}
