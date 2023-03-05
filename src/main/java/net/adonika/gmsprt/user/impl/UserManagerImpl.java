package net.adonika.gmsprt.user.impl;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.exception.FieldError;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.user.dao.UserDao;

@Service("userManager")
public class UserManagerImpl implements UserManager {

    private final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDao userDao;
    private final MessageSource messageSource;

    public UserManagerImpl(UserDao userDao, MessageSource messageSource) {
        this.userDao = userDao;
        this.messageSource = messageSource;
    }

    @Override
    public UserInfo create(UserInfo userInfo) {

        if (userInfo.getSeqUser() != null && userInfo.getSeqUser() > 0) {
            throw ErrorResp.getConflict(
                    new FieldError(
                            "seqUser", userInfo.getSeqUser(),
                            messageSource.getMessage("validation.is_null", new String[]{"userInfo.seqUser"}, Locale.getDefault())
                    )
            );
        }

        return userDao.save(userInfo);
    }

    @Override
    public UserInfo getUserInfo(Long seqUser) {
        return userDao.findById(seqUser).orElse(null);
    }

    @Override
    public UserInfo update(UserInfo userInfo, List<String> ignores) {

        UserInfo savedUser = userDao.findById(userInfo.getSeqUser()).orElseThrow(
                () -> ErrorResp.getNotFound(
                        new FieldError(
                                "seqUser", userInfo.getSeqUser(),
                                messageSource.getMessage("exception.not_found", null, Locale.getDefault())
                        )
                )
        );

        logger.debug("before userInfo: {}", savedUser.toString());
        BeanUtils.copyProperties(userInfo, savedUser, ignores.toArray(new String[0]));
        logger.debug("after userInfo: {}", savedUser.toString());

        return userDao.save(savedUser);
    }
}
