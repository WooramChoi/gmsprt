package net.adonika.gmsprt.user.impl;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.user.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("userManager")
public class UserManagerImpl implements UserManager {

    private final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDao userDao;

    public UserManagerImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserInfo create(String name, String email, String urlPicture) {

        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setEmail(email);
        userInfo.setUrlPicture(urlPicture);

        return userDao.save(userInfo);
    }

    @Override
    public UserInfo getUserInfo(Long seqUser) {
        return userDao.findById(seqUser).orElse(null);
    }

    @Override
    public UserInfo update(UserInfo userInfo, List<String> ignores) {

        // TODO MessageSource
        Assert.notNull(userInfo, "userInfo must be not null");
        Assert.notNull(userInfo.getSeqUser(), "userInfo.seqUser must be not null");

        // TODO MessageSource
        UserInfo savedUser = userDao.findById(userInfo.getSeqUser()).orElseThrow(() -> new NullPointerException("user not found"));

        logger.debug("before userInfo: {}", savedUser.toString());
        BeanUtils.copyProperties(userInfo, savedUser, ignores.toArray(new String[0]));
        logger.debug("after userInfo: {}", savedUser.toString());

        return userDao.save(savedUser);
    }
}
