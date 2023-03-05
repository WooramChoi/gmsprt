package net.adonika.gmsprt.user.impl;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.exception.FieldError;
import net.adonika.gmsprt.user.UserProfileManager;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.user.dao.UserProfileDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Locale;

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

    @Override
    public UserProfileInfo getUserProfile(Long seqUserProfile) {
        return userProfileDao.findById(seqUserProfile).orElse(null);
    }

    @Override
    public UserProfileInfo getUserProfile(String provider, String sid) {
        return userProfileDao.findByProviderAndSid(provider, sid);
    }

    @Override
    public UserProfileInfo create(UserProfileInfo userProfileInfo, Long seqUser) {

        if (userProfileInfo.getSeqUserProfile() != null && userProfileInfo.getSeqUserProfile() > 0) {
            throw ErrorResp.getConflict(
                    new FieldError(
                            "seqUserProfile", userProfileInfo.getSeqUserProfile(),
                            messageSource.getMessage("validation.is_null", new String[]{"userProfileInfo.seqUserProfile"}, Locale.getDefault())
                    ));
        }

        UserInfo userInfo = userDao.findById(seqUser).orElseThrow(
                () -> ErrorResp.getNotFound(
                        new FieldError(
                                "seqUser", seqUser,
                                messageSource.getMessage("exception.not_found", null, Locale.getDefault())
                        )
                )
        );
        userProfileInfo.setUserInfo(userInfo);

        return userProfileDao.save(userProfileInfo);
    }

    @Override
    public UserProfileInfo update(UserProfileInfo userProfileInfo, List<String> ignores) {

        // TODO MessageSource
        UserProfileInfo savedUserProfile = userProfileDao.findById(userProfileInfo.getSeqUserProfile()).orElseThrow(
                () -> ErrorResp.getNotFound(
                        new FieldError(
                                "seqUserProfile", userProfileInfo.getSeqUserProfile(),
                                messageSource.getMessage("exception.not_found", null, Locale.getDefault())
                        )
                )
        );

        logger.debug("before userProfileInfo: {}", savedUserProfile.toString());
        BeanUtils.copyProperties(userProfileInfo, savedUserProfile, ignores.toArray(new String[0]));
        logger.debug("after userProfileInfo: {}", savedUserProfile.toString());

        return userProfileDao.save(userProfileInfo);
    }
}
