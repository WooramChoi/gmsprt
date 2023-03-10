package net.adonika.gmsprt.user.impl;

import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserForm;
import net.adonika.gmsprt.user.model.UserModify;
import net.adonika.gmsprt.user.model.UserVO;

@Service("userManager")
public class UserManagerImpl implements UserManager {

    private final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDao userDao;
    private final MessageSource messageSource;

    public UserManagerImpl(UserDao userDao, MessageSource messageSource) {
        this.userDao = userDao;
        this.messageSource = messageSource;
    }

    private UserVO convertTo(UserInfo userInfo) {
        UserVO instance = new UserVO();
        
        BeanUtils.copyProperties(userInfo, instance);
        
        return instance;
    }
    
    @Transactional
    @Override
    public UserVO addUser(UserAdd userAdd) {
        logger.info("[addUser] start");
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userAdd, userInfo);
        
        UserInfo savedUserInfo = userDao.save(userInfo);
        logger.info("[addUser] done: seqUser = {}", savedUserInfo.getSeqUser());
        return convertTo(savedUserInfo);
    }

    @Transactional
    @Override
    public UserVO modifyUser(Long seqUser, UserModify userModify) {
        logger.info("[modifyUser] start: seqUser = {}", seqUser);
        UserInfo userInfo = userDao.findById(seqUser).orElseThrow(()->{
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqUser", seqUser, messageSource.getMessage("validation.user.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        BeanUtils.copyProperties(userModify, userInfo, userModify.getIgnores());
        
        UserInfo savedUserInfo = userDao.saveAndFlush(userInfo);
        logger.info("[modifyUser] done: seqUser = {}", savedUserInfo.getSeqUser());
        return convertTo(savedUserInfo);
    }

    @Override
    public void removeUser(Long seqUser) {
        // TODO Auto-generated method stub
        
    }

    @Transactional
    @Override
    public UserVO findUser(Long seqUser) {
        logger.info("[findUser] start: seqUser = {}", seqUser);
        UserInfo savedUserInfo= userDao.findById(seqUser).orElseThrow(()->{
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqUser", seqUser, messageSource.getMessage("validation.user.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        logger.info("[findUser] done: seqUser = {}", seqUser);
        return convertTo(savedUserInfo);
    }

    @Override
    public List<UserVO> findUser(UserForm userForm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<UserVO> findUser(UserForm userForm, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }
}
