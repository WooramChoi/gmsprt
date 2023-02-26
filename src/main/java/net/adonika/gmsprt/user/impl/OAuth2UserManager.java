package net.adonika.gmsprt.user.impl;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.security.model.OAuth2UserInfo;
import net.adonika.gmsprt.security.model.OAuth2UserInfoFactory;
import net.adonika.gmsprt.security.model.OAuth2UserPrincipal;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.user.UserProfileManager;
import net.adonika.gmsprt.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class OAuth2UserManager extends DefaultOAuth2UserService {

    private final Logger logger = LoggerFactory.getLogger(OAuth2UserManager.class);

    private final UserManager userManager;
    private final UserProfileManager userProfileManager;

    public OAuth2UserManager(UserManager userManager, UserProfileManager userProfileManager) {
        this.userManager = userManager;
        this.userProfileManager = userProfileManager;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {

        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        logger.info("{}에서 로그인 완료", provider);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        logger.info("- {}({})", oAuth2UserInfo.getName(), oAuth2UserInfo.getSid());

        /* <!-- DEBUG --> */
        logger.debug("## Start Attributes");
        Map<String, Object> attributes = oAuth2User.getAttributes();
        for (String key : attributes.keySet()) {
            logger.debug("- {} : {}", key, attributes.get(key));
        }
        logger.debug("## End Attributes");
        /* <!-- //DEBUG --> */

        /* <!-- Get Principal --> */
        OAuth2UserPrincipal savedPrincipal = null;
        Long seqUser = null;

        // TODO SecurityContextHolder 외에 Authentication 을 가져올 방법이 없을까?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null && principal.getClass() == OAuth2UserPrincipal.class){
                savedPrincipal = (OAuth2UserPrincipal) principal;
                seqUser = savedPrincipal.getSeqUser();
            }
        }
        /* <!-- //Get Principal --> */

        /* <!-- Save User --> */
        UserProfileInfo savedUserProfile = userProfileManager.getUserProfile(provider, oAuth2UserInfo.getSid());
        if (savedUserProfile == null) {
            logger.info("신규회원, 저장 시도");
            savedUserProfile = createUser(seqUser, provider, oAuth2UserInfo);
            seqUser = savedUserProfile.getUserInfo().getSeqUser();
            logger.info("신규회원, 저장 완료: {}({}) -> {}", savedUserProfile.getName(), savedUserProfile.getSid(), savedUserProfile.getSeqUserProfile());
        } else {
            logger.info("기존회원[{}], 저장 시도", seqUser);
            savedUserProfile = updateUser(savedUserProfile, oAuth2UserInfo);
            seqUser = savedUserProfile.getUserInfo().getSeqUser();
            /*
                TODO 조회된 UserProfile.UserInfo.seqUser 와 Principal.seqUser 를 비교해야한다
                 -> 이미 user 가 생성된 userProfile 을 나중에 연결하려는 케이스
                 -> 우선은 Principal 을 갱신하도록함
             */
            savedPrincipal = null;
            logger.info("기존회원, 저장 완료: {}({}) -> {}", savedUserProfile.getName(), savedUserProfile.getSid(), savedUserProfile.getSeqUserProfile());
        }
        /* <!-- //Save User --> */

        /* <!-- Set Principal --> */
        if (savedPrincipal != null) {
            logger.debug("Exist Principal:");
            logger.debug("\t [{}] before provider: {}", savedPrincipal.getName(), savedPrincipal.getActiveProvider());
            savedPrincipal.putOAuth2User(provider, oAuth2User);
        } else {
            logger.debug("New Principal");
            savedPrincipal = new OAuth2UserPrincipal(seqUser, provider, oAuth2User);
        }
        /* <!-- //Set Principal --> */

        return savedPrincipal;
    }

    private UserProfileInfo createUser(Long seqUser, String provider, OAuth2UserInfo oAuth2UserInfo) {

        UserInfo savedUser = null;
        if (seqUser == null) {
            logger.info("신규 유저 정보 생성");
            savedUser = userManager.create(oAuth2UserInfo.getName(), oAuth2UserInfo.getEmail(), oAuth2UserInfo.getUrlPicture());
            logger.info("신규 유저 정보 생성 완료: [{}] {}", savedUser.getSeqUser(), savedUser.getName());
        } else {
            logger.info("기존 유저 정보 조회");
            savedUser = userManager.getUserInfo(seqUser);
            logger.info("기존 유저 정보 조회 완료: [{}] {}", savedUser.getSeqUser(), savedUser.getName());
        }

        return userProfileManager.create(
                provider, oAuth2UserInfo.getSid(), oAuth2UserInfo.getUid(),
                oAuth2UserInfo.getName(), oAuth2UserInfo.getEmail(), oAuth2UserInfo.getUrlPicture(),
                savedUser.getSeqUser()
        );
    }

    private UserProfileInfo updateUser(UserProfileInfo savedUserProfile, OAuth2UserInfo oAuth2UserInfo) {

        savedUserProfile.setName(oAuth2UserInfo.getName());
        savedUserProfile.setEmail(oAuth2UserInfo.getEmail());
        savedUserProfile.setUrlPicture(oAuth2UserInfo.getUrlPicture());

        // TODO 서비스에 ignores 를 일일히 전달해주기 불편하다
        List<String> ignores = ObjectUtil.getFieldNames(UserProfileInfo.class);
        ignores.removeAll(Arrays.asList("name", "email", "urlPicture"));

        return userProfileManager.update(savedUserProfile, ignores);
    }

}
