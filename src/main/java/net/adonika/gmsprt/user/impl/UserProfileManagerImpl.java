package net.adonika.gmsprt.user.impl;

import java.util.List;
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
        
        /*
         * TODO Entity 객체 내에 "userInfo" 라고 필드명을 정의해야하는 상황
         */
        BeanUtils.copyProperties(userProfileInfo, instance, "userInfo");
        logger.debug("[convertTo] copy userProfileInfo to UserProfileVO done: {}", ObjectUtil.toJson(instance));
        
        UserInfo userInfo = userProfileInfo.getUserInfo();
        if (userInfo != null) {
            logger.debug("[convertTo] userProfileInfo has UserInfo: seqUser = {}", userInfo.getSeqUser());
            
            try {
                /*
                 * TODO VO 객체 내에 "user" 라고 필드명을 정의해야 userInfo 에 연결되는 상황
                 */
                ObjectUtil.copyToField(instance, "user", userInfo);
                logger.debug("[convertTo] copy userInfo to \"user\" done: {}", ObjectUtil.toJson(instance));
            } catch (NoSuchFieldException e) {
                // TODO Auto-generated catch block
                logger.error("[convertTo] UserProfileVO hasn't field of \"user\"");
            }
        }
        
        return instance;
    }

    @Transactional
    @Override
    public UserProfileVO addUserProfile(UserProfileAdd userProfileAdd, Long seqUser) {
        logger.info("[addUserProfile] start");
        UserProfileInfo userProfileInfo = new UserProfileInfo();
        BeanUtils.copyProperties(userProfileAdd, userProfileInfo);
        
        if (Optional.ofNullable(seqUser).orElse(0L) > 0L) {
            logger.info("[addUserProfile] has UserInfo: seqUser = {}", seqUser);
            UserInfo userInfo = userDao.getById(seqUser);
            userProfileInfo.setUserInfo(userInfo);
            logger.info("[addUserProfile] set UserInfo done");
        }
        
        UserProfileInfo savedUserProfileInfo = userProfileDao.save(userProfileInfo);
        logger.info("[addUserProfile] done: seqUserProfile = {}", savedUserProfileInfo.getSeqUserProfile());
        return convertTo(savedUserProfileInfo);
    }

    @Transactional
    @Override
    public UserProfileVO modifyUserProfile(Long seqUserProfile, UserProfileModify userProfileModify, Long seqUser) {
        logger.info("[modifyUserProfile] start: seqUserProfile = {} / seqUser = {}", seqUserProfile, seqUser);
        UserProfileInfo userProfileInfo = userProfileDao.getById(seqUserProfile);
        BeanUtils.copyProperties(userProfileModify, userProfileInfo, userProfileModify.getIgnores());
        
        // 연결된 유저를 변경하는 상황은 없을것이라고 간주.
//        if (Optional.ofNullable(seqUser).orElse(0L) > 0L) {
//            logger.info("[modifyUserProfile] has UserInfo: seqUser = {}", seqUser);
//            UserInfo userInfo = userDao.getById(seqUser);
//            userProfileInfo.setUserInfo(userInfo);
//            logger.info("[modifyUserProfile] set UserInfo done");
//        }
        
        UserProfileInfo savedUserProfileInfo = userProfileDao.save(userProfileInfo);
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
        UserProfileInfo savedUserProfileInfo = userProfileDao.getById(seqUserProfile);
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
        UserProfileInfo savedUserProfileInfo = userProfileDao.findByProviderAndSid(provider, sid).orElse(null);
        logger.info("[findUserProfile] done: provider = {} / sid = {}", provider, sid);
        if (savedUserProfileInfo == null) {
            return null;
        } else {
            return convertTo(savedUserProfileInfo);
        }
    }
}
