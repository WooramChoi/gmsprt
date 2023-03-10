package net.adonika.gmsprt.user.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.user.UserProfileManager;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.user.dao.UserProfileDao;
import net.adonika.gmsprt.user.model.UserProfileAdd;
import net.adonika.gmsprt.user.model.UserProfileForm;
import net.adonika.gmsprt.user.model.UserProfileModify;
import net.adonika.gmsprt.user.model.UserProfileVO;
import net.adonika.gmsprt.util.ObjectUtil;

@Service("userProfileManager")
public class UserProfileManagerImpl implements UserProfileManager {

    private final Logger logger = LoggerFactory.getLogger(UserProfileManagerImpl.class);

    private final UserProfileDao userProfileDao;
    private final UserDao userDao;
    private final MessageSource messageSource;

    public UserProfileManagerImpl(UserProfileDao userProfileDao, UserDao userDao, MessageSource messageSource) {
        this.userProfileDao = userProfileDao;
        this.userDao = userDao;
        this.messageSource = messageSource;
    }
    
    private UserProfileVO convertTo(UserProfileInfo userProfileInfo) {
        logger.debug("[convertTo] userProfileInfo to UserProfileVO start");
        UserProfileVO instance = new UserProfileVO();
        
        BeanUtils.copyProperties(userProfileInfo, instance, "userInfo");
        logger.debug("[convertTo] copy userProfileInfo to UserProfileVO done: {}", ObjectUtil.toJson(instance));
        
        UserInfo userInfo = userProfileInfo.getUserInfo();
        if (userInfo != null) {
            logger.debug("[convertTo] userProfileInfo has UserInfo: seqUser = {}", userInfo.getSeqUser());
            
            try {
                ObjectUtil.copyToField(instance, "user", userInfo);
                logger.debug("[convertTo] copy userInfo to \"user\" done: {}", ObjectUtil.toJson(instance));
            } catch (NoSuchFieldException e) {
                logger.error("[convertTo] UserProfileVO hasn't field of \"user\"");
            }
        }
        
        return instance;
    }

    @Transactional
    @Override
    public UserProfileVO addUserProfile(UserProfileAdd userProfileAdd) {
        logger.info("[addUserProfile] start");
        UserProfileInfo userProfileInfo = new UserProfileInfo();
        BeanUtils.copyProperties(userProfileAdd, userProfileInfo);

        Long seqUser = userProfileAdd.getSeqUser();
        if (Optional.ofNullable(seqUser).orElse(0L) > 0L) {
            logger.info("[addUserProfile] has UserInfo: seqUser = {}", seqUser);
            UserInfo userInfo = userDao.findById(seqUser).orElseThrow(()->{
                ErrorResp errorResp = ErrorResp.getNotFound();
                errorResp.addError("seqUser", seqUser, messageSource.getMessage("validation.user.not_found", null, Locale.getDefault()));
                return errorResp;
            });
            userProfileInfo.setUserInfo(userInfo);
            logger.info("[addUserProfile] set UserInfo done");
        }
        
        UserProfileInfo savedUserProfileInfo = userProfileDao.save(userProfileInfo);
        logger.info("[addUserProfile] done: seqUserProfile = {}", savedUserProfileInfo.getSeqUserProfile());
        return convertTo(savedUserProfileInfo);
    }

    @Transactional
    @Override
    public UserProfileVO modifyUserProfile(Long seqUserProfile, UserProfileModify userProfileModify) {
        logger.info("[modifyUserProfile] start: seqUserProfile = {}", seqUserProfile);
        UserProfileInfo userProfileInfo = userProfileDao.findById(seqUserProfile).orElseThrow(()->{
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqUserProfile", seqUserProfile, messageSource.getMessage("validation.user_profile.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        BeanUtils.copyProperties(userProfileModify, userProfileInfo, userProfileModify.getIgnores());
        
        UserProfileInfo savedUserProfileInfo = userProfileDao.saveAndFlush(userProfileInfo);
        logger.info("[modifyUserProfile] done: seqUserProfile = {}", savedUserProfileInfo.getSeqUserProfile());
        return convertTo(savedUserProfileInfo);
    }

    @Override
    public void removeUserProfile(Long seqUserProfile) {
        // TODO Auto-generated method stub
        
    }

    @Transactional
    @Override
    public UserProfileVO findUserProfile(Long seqUserProfile) {
        logger.info("[findUserProfile] start: seqUserProfile = {}", seqUserProfile);
        UserProfileInfo savedUserProfileInfo = userProfileDao.findById(seqUserProfile).orElseThrow(()->{
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqUserProfile", seqUserProfile, messageSource.getMessage("validation.user_profile.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        logger.info("[findUserProfile] done: seqUserProfile = {}", seqUserProfile);
        return convertTo(savedUserProfileInfo);
    }

    @Override
    public List<UserProfileVO> findUserProfile(UserProfileForm userProfileForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<UserProfileVO> findUserProfile(UserProfileForm userProfileForm, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    @Override
    public UserProfileVO findUserProfile(String provider, String sid) {
        logger.info("[findUserProfile] start: provider = {} / sid = {}", provider, sid);
        UserProfileInfo savedUserProfileInfo = userProfileDao.findByProviderAndSid(provider, sid).orElseThrow(()->{
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("provider", provider, messageSource.getMessage("validation.user_profile.not_found", null, Locale.getDefault()));
            errorResp.addError("sid", sid, messageSource.getMessage("validation.user_profile.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        logger.info("[findUserProfile] done: provider = {} / sid = {}", provider, sid);
        return convertTo(savedUserProfileInfo);
    }
}
