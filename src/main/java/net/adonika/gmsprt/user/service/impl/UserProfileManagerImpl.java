package net.adonika.gmsprt.user.service.impl;

import net.adonika.gmsprt.domain.AuthTokenId;
import net.adonika.gmsprt.domain.AuthTokenInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.security.dao.AuthTokenDao;
import net.adonika.gmsprt.security.service.AuthTokenManager;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.user.dao.UserProfileDao;
import net.adonika.gmsprt.user.model.UserProfileAdd;
import net.adonika.gmsprt.user.model.UserProfileModify;
import net.adonika.gmsprt.user.model.UserProfileVO;
import net.adonika.gmsprt.user.service.UserProfileManager;
import net.adonika.gmsprt.util.DateUtil;
import net.adonika.gmsprt.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service("userProfileManager")
public class UserProfileManagerImpl implements UserProfileManager, AuthTokenManager {

    private final Logger logger = LoggerFactory.getLogger(UserProfileManagerImpl.class);

    private final UserProfileDao userProfileDao;
    private final UserDao userDao;
    private final MessageSource messageSource;
    
    private final AuthTokenDao authTokenDao;
    private final WebClient googleApiClient;
    private final WebClient githubApiClient;

    public UserProfileManagerImpl(UserProfileDao userProfileDao,
            UserDao userDao,
            MessageSource messageSource,
            AuthTokenDao authTokenDao,
            @Qualifier("googleApiClient") WebClient googleOAuthClient,
            @Qualifier("githubApiClient") WebClient githubApiClient) {
        
        this.userProfileDao = userProfileDao;
        this.userDao = userDao;
        this.messageSource = messageSource;
        this.authTokenDao = authTokenDao;
        this.googleApiClient = googleOAuthClient;
        this.githubApiClient = githubApiClient;
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
        
        if (userProfileModify.getSeqUser() != null && userProfileModify.getSeqUser() > 0L) {
            UserInfo userInfo = userDao.findById(userProfileModify.getSeqUser()).orElseThrow(()->{
                ErrorResp errorResp = ErrorResp.getNotFound();
                errorResp.addError("seqUser", userProfileModify.getSeqUser(), messageSource.getMessage("validation.user.not_found", null, Locale.getDefault()));
                return errorResp;
            });
            userProfileInfo.setUserInfo(userInfo);
        }
        
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
    public List<UserProfileVO> findUserProfile(Long seqUser) {
        logger.info("[findUserProfile] start: seqUSer={}", seqUser);
        List<UserProfileInfo> savedUserProfiles = userProfileDao.findByUserInfo_SeqUser(seqUser);
        logger.info("[findUserProfile] done: savedUserProfiles[{}]", savedUserProfiles.size());
        List<UserProfileVO> list = new ArrayList<>();
        savedUserProfiles.forEach(userProfileInfo -> list.add(convertTo(userProfileInfo)));
        return list;
    }

    @Transactional
    @Override
    public Optional<UserProfileVO> saveAuthToken(AuthTokenId authTokenId) {
        logger.info("[saveAuthToken] start: authTokenId = {}", ObjectUtil.toJson(authTokenId));
        String registrationId = authTokenId.getRegistrationId();
        String accessToken = authTokenId.getAccessToken();
        
        logger.info("[saveAuthToken] find authTokenInfo");
        AuthTokenInfo authTokenInfo = authTokenDao.findById(authTokenId).orElse(null);
        if (authTokenInfo == null) {
            logger.info("[saveAuthToken] not found. create new entity");
            authTokenInfo = new AuthTokenInfo();
            authTokenInfo.setRegistrationId(registrationId);
            authTokenInfo.setAccessToken(accessToken);
        } else {
            logger.info("[saveAuthToken] found. created at {} / updated at {}", DateUtil.dateToString(authTokenInfo.getDtCreate()), DateUtil.dateToString(authTokenInfo.getDtUpdate()));
        }
        
        // WebClient 를 통해 AccessToken 을 이용 사용자 정보 조회
        // TODO 가능하면 함수로 분리할 것
        logger.info("[saveAuthToken] get [{}] user info by api", registrationId);
        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<Map<String, Object>>(){};
        String sid, uid, name, email, urlPicture;
        if ("google".equals(registrationId)) {

            Map<String, Object> attributes = googleApiClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth2/v3/userinfo")
                            .queryParam("alt", "json")
                            .build())
                    .headers(httpHeaders -> {
                        httpHeaders.setBearerAuth(accessToken);
                    })
                    .retrieve()
                    .bodyToMono(typeReference)
                    .block();

            // NOTE 어차피 attributes 가 null 이면, throw 된 상황이다.
            if (attributes == null) {
                attributes = new HashMap<>();
            }

            sid = Objects.toString(attributes.get("sub"), null);
            uid = Objects.toString(attributes.get("sub"), null);
            name = Objects.toString(attributes.get("name"), null);
            email = Objects.toString(attributes.get("email"), null);
            urlPicture = Objects.toString(attributes.get("picture"), null);
            
        } else if ("github".equals(registrationId)) {

            Map<String, Object> attributes = githubApiClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/user")
                            .build())
                    .headers(httpHeaders -> {
                        httpHeaders.setBearerAuth(accessToken);
                    })
                    .retrieve()
                    .bodyToMono(typeReference)
                    .block();

            if (attributes == null) {
                attributes = new HashMap<>();
            }
            
            sid = Objects.toString(attributes.get("id"), null);
            uid = Objects.toString(attributes.get("login"), null);
            name = Objects.toString(attributes.get("name"), null);
            email = Objects.toString(attributes.get("email"), null);
            urlPicture = Objects.toString(attributes.get("avatar_url"), null);

        } else if ("kakao".equals(registrationId)) {

            sid = null;
            uid = null;
            name = null;
            email = null;
            urlPicture = null;
            
        } else {
            
            sid = null;
            uid = null;
            name = null;
            email = null;
            urlPicture = null;
            
        }
        logger.info("[saveAuthToken] received user: sid={}", sid);
        logger.debug("[saveAuthToken] uid={} / name={} / email={} urlPicture={}", uid, name, email, urlPicture);
        
        // TODO OAuth2UserManager 를 참고, userProfile / user 저장
        logger.info("[saveAuthToken] find userProfileInfo by provider={} / sid={}", registrationId, sid);
        UserProfileInfo userProfileInfo = userProfileDao.findByProviderAndSid(registrationId, sid).orElse(null);
        if (userProfileInfo == null) {
            logger.info("[saveAuthToken] not found. create new entity");
            
            userProfileInfo = new UserProfileInfo();
            userProfileInfo.setProvider(registrationId);
            userProfileInfo.setSid(sid);
            userProfileInfo.setUid(uid);
        } else {
            logger.info("[saveAuthToken] found. seqUserProfile={}", userProfileInfo.getSeqUserProfile());
        }
        
        userProfileInfo.setName(name);
        userProfileInfo.setEmail(email);
        userProfileInfo.setUrlPicture(urlPicture);
        
        UserProfileInfo savedUserProfile = userProfileDao.save(userProfileInfo);
        logger.info("[saveAuthToken] save userProfile, done: seqUserProfile={}", savedUserProfile.getSeqUserProfile());
        authTokenInfo.setUserProfileInfo(savedUserProfile);
        
        AuthTokenInfo savedAuthToken = authTokenDao.save(authTokenInfo);
        logger.info("[saveAuthToken] done: authTokenId = {}", ObjectUtil.toJson(authTokenId));
        
        return Optional.of(convertTo(savedUserProfile));
    }

    @Transactional
    @Override
    public Optional<UserProfileVO> findUserProfile(AuthTokenId authTokenId) {
        logger.info("[findUserProfile] start: authTokenId = {}", ObjectUtil.toJson(authTokenId));
        
        UserProfileVO userProfileVO;
        UserProfileInfo savedUserProfileInfo;
        AuthTokenInfo authTokenInfo = authTokenDao.findById(authTokenId).orElse(null);
        if (authTokenInfo != null) {
            savedUserProfileInfo = authTokenInfo.getUserProfileInfo();
        } else {
            savedUserProfileInfo = null;
        }
        
        logger.info("[findUserProfile] finded? {}", savedUserProfileInfo != null);
        
        if (savedUserProfileInfo != null) {
            userProfileVO = convertTo(savedUserProfileInfo);
        } else {
            userProfileVO = null;
        }
        
        logger.info("[findUserProfile] done: authTokenId = {}", ObjectUtil.toJson(authTokenId));
        return Optional.ofNullable(userProfileVO);
    }

    @Override
    public void removeAuthToken(AuthTokenId authTokenId) {
        // TODO Auto-generated method stub
        
    }
}
