package net.adonika.gmsprt.user.service.impl;

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
import net.adonika.gmsprt.user.service.UserManager;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserSearch;
import net.adonika.gmsprt.user.model.UserModify;
import net.adonika.gmsprt.user.model.UserDetails;

@Service("userManager")
public class UserManagerImpl implements UserManager {

    private final Logger logger = LoggerFactory.getLogger(UserManagerImpl.class);

    private final UserDao userDao;
    private final MessageSource messageSource;

    public UserManagerImpl(UserDao userDao, MessageSource messageSource) {
        this.userDao = userDao;
        this.messageSource = messageSource;
    }

    private UserDetails convertTo(UserInfo userInfo) {
        UserDetails instance = new UserDetails();
        
        BeanUtils.copyProperties(userInfo, instance);
        
        return instance;
    }
    
    @Transactional
    @Override
    public UserDetails addUser(UserAdd userAdd) {
        logger.info("[addUser] start");
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userAdd, userInfo);
        
        UserInfo savedUserInfo = userDao.save(userInfo);
        logger.info("[addUser] done: seqUser = {}", savedUserInfo.getSeqUser());
        return convertTo(savedUserInfo);
    }

    @Transactional
    @Override
    public UserDetails modifyUser(Long seqUser, UserModify userModify) {
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
    public UserDetails findUser(Long seqUser) {
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
    public List<UserDetails> findUser(UserSearch userSearch) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<UserDetails> findUser(UserSearch userSearch, Pageable pageable) {
        // TODO Auto-generated method stub
        return null;
    }
}
