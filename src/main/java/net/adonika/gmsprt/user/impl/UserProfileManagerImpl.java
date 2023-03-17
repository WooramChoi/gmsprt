package net.adonika.gmsprt.user.impl;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import net.adonika.gmsprt.domain.AuthTokenId;
import net.adonika.gmsprt.domain.AuthTokenInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.security.dao.AuthTokenDao;
import net.adonika.gmsprt.security.service.AuthTokenManager;
import net.adonika.gmsprt.user.UserProfileManager;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.user.dao.UserProfileDao;
import net.adonika.gmsprt.user.model.UserProfileAdd;
import net.adonika.gmsprt.user.model.UserProfileForm;
import net.adonika.gmsprt.user.model.UserProfileModify;
import net.adonika.gmsprt.user.model.UserProfileVO;
import net.adonika.gmsprt.user.model.UserVO;
import net.adonika.gmsprt.util.DateUtil;
import net.adonika.gmsprt.util.ObjectUtil;

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
        logger.info("[saveAuthToken] get [{}] user info by api", registrationId);
        ParameterizedTypeReference<Map<String, Object>> typeReference = new ParameterizedTypeReference<Map<String, Object>>(){};
        String sid, uid, name, email, urlPicture;
        if ("google".equals(registrationId)) {
            /*
             * {
             *    "id": "102693227186529279417",
             *    "email": "dnfka4042@gmail.com",
             *    "verified_email": true,
             *    "name": "Wooram Choi",
             *    "given_name": "Wooram",
             *    "family_name": "Choi",
             *    "picture": "https://lh3.googleusercontent.com/a/AGNmyxbXwOPSeOLpGz-dZr4RiQxYkpGEKkMxfACRsQw7iA=s96-c",
             *    "locale": "ko"
             *  }
             */
            Map<String, Object> attributes = googleApiClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/oauth2/v1/userinfo")
                            .queryParam("alt", "json")
                            .build())
                    .headers(httpHeaders -> {
                        httpHeaders.setBearerAuth(accessToken);
                    })
                    .retrieve()
                    .bodyToMono(typeReference)
                    .block();
            
            sid = attributes.get("id").toString();
            uid = attributes.get("id").toString();
            name = attributes.get("name").toString();
            email = attributes.get("email").toString();
            urlPicture = attributes.get("picture").toString();
            
        } else if ("github".equals(registrationId)) {
            
            /*
             * {
             *    "login": "WooramChoi",
             *    "id": 42209000,
             *    "node_id": "MDQ6VXNlcjQyMjA5MDAw",
             *    "avatar_url": "https://avatars.githubusercontent.com/u/42209000?v=4",
             *    "gravatar_id": "",
             *    "url": "https://api.github.com/users/WooramChoi",
             *    "html_url": "https://github.com/WooramChoi",
             *    "followers_url": "https://api.github.com/users/WooramChoi/followers",
             *    "following_url": "https://api.github.com/users/WooramChoi/following{/other_user}",
             *    "gists_url": "https://api.github.com/users/WooramChoi/gists{/gist_id}",
             *    "starred_url": "https://api.github.com/users/WooramChoi/starred{/owner}{/repo}",
             *    "subscriptions_url": "https://api.github.com/users/WooramChoi/subscriptions",
             *    "organizations_url": "https://api.github.com/users/WooramChoi/orgs",
             *    "repos_url": "https://api.github.com/users/WooramChoi/repos",
             *    "events_url": "https://api.github.com/users/WooramChoi/events{/privacy}",
             *    "received_events_url": "https://api.github.com/users/WooramChoi/received_events",
             *    "type": "User",
             *    "site_admin": false,
             *    "name": "Wooram, Choi",
             *    "company": null,
             *    "blog": "",
             *    "location": null,
             *    "email": "dnfka404@naver.com",
             *    "hireable": null,
             *    "bio": null,
             *    "twitter_username": null,
             *    "public_repos": 3,
             *    "public_gists": 0,
             *    "followers": 0,
             *    "following": 0,
             *    "created_at": "2018-08-08T12:21:28Z",
             *    "updated_at": "2023-02-26T13:04:39Z"
             *  }
             */
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
            
            sid = attributes.get("id").toString();
            uid = attributes.get("login").toString();
            name = attributes.get("name").toString();
            email = attributes.get("email").toString();
            urlPicture = attributes.get("avatar_url").toString();
            
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
