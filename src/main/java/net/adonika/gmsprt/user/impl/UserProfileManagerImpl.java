package net.adonika.gmsprt.user.impl;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.user.UserProfileManager;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.user.dao.UserProfileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("userProfileManager")
public class UserProfileManagerImpl implements UserProfileManager {

    private final Logger logger = LoggerFactory.getLogger(UserProfileManagerImpl.class);

    private final UserProfileDao userProfileDao;

    private final UserDao userDao;

    public UserProfileManagerImpl(UserProfileDao userProfileDao, UserDao userDao) {
        this.userProfileDao = userProfileDao;
        this.userDao = userDao;
    }

    @Override
    public UserProfileInfo getUserProfile(Long seqUserProfile) {
        return userProfileDao.findById(seqUserProfile).orElse(null);
    }

    @Override
    public UserProfileInfo getUserProfile(String provider, String sid) {
        return userProfileDao.findByProviderAndSid(provider, sid);
    }

    @Override
    public UserProfileInfo create(String provider, String sid, String uid, String name, String email, String urlPicture, Long seqUser) {
        UserProfileInfo userProfileInfo = new UserProfileInfo();
        userProfileInfo.setProvider(provider);
        userProfileInfo.setSid(sid);
        userProfileInfo.setUid(uid);

        userProfileInfo.setName(name);
        userProfileInfo.setEmail(email);
        userProfileInfo.setUrlPicture(urlPicture);

        // TODO MessageSource
        UserInfo userInfo = userDao.findById(seqUser).orElseThrow(() -> new NullPointerException("user not found"));
        userProfileInfo.setUserInfo(userInfo);

        return userProfileDao.save(userProfileInfo);
    }

    @Override
    public UserProfileInfo update(UserProfileInfo userProfileInfo, List<String> ignores) {

        // TODO MessageSource
        Assert.notNull(userProfileInfo, "userProfileInfo must be not null");
        Assert.notNull(userProfileInfo.getSeqUserProfile(), "userProfileInfo.seqUserProfile must be not null");

        // TODO MessageSource
        UserProfileInfo savedUserProfile = userProfileDao.findById(userProfileInfo.getSeqUserProfile()).orElseThrow(() -> new NullPointerException("user profile not found"));

        logger.debug("before userProfileInfo: {}", savedUserProfile.toString());
        BeanUtils.copyProperties(userProfileInfo, savedUserProfile, ignores.toArray(new String[0]));
        logger.debug("after userProfileInfo: {}", savedUserProfile.toString());

        return userProfileDao.save(userProfileInfo);
    }
}
