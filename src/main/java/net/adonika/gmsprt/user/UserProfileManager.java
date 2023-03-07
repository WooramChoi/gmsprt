package net.adonika.gmsprt.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.adonika.gmsprt.user.model.UserProfileAdd;
import net.adonika.gmsprt.user.model.UserProfileForm;
import net.adonika.gmsprt.user.model.UserProfileModify;
import net.adonika.gmsprt.user.model.UserProfileVO;

public interface UserProfileManager {
    
    UserProfileVO addUserProfile(UserProfileAdd userProfileAdd, Long seqUser);
    
    UserProfileVO modifyUserProfile(Long seqUserProfile, UserProfileModify userProfileModify, Long seqUser);
    
    void removeUserProfile(Long seqUserProfile);
    
    UserProfileVO findUserProfile(Long seqUserProfile);
    
    List<UserProfileVO> findUserProfile(UserProfileForm userProfileForm);
    
    Page<UserProfileVO> findUserProfile(UserProfileForm userProfileForm, Pageable pageable);
    
    UserProfileVO findUserProfile(String provider, String sid);

}
