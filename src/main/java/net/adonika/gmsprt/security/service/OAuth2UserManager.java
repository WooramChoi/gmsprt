package net.adonika.gmsprt.security.service;

import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.security.model.OAuth2UserInfo;
import net.adonika.gmsprt.security.model.OAuth2UserInfoFactory;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.user.UserProfileManager;
import net.adonika.gmsprt.user.model.*;
import net.adonika.gmsprt.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserManager
//extends DefaultOAuth2UserService
{

    private final Logger logger = LoggerFactory.getLogger(OAuth2UserManager.class);

    private final UserManager userManager;
    private final UserProfileManager userProfileManager;

    public OAuth2UserManager(UserManager userManager, UserProfileManager userProfileManager) {
        this.userManager = userManager;
        this.userProfileManager = userProfileManager;
    }

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        return processOAuth2User(userRequest, oAuth2User);
//    }

//    public OAuth2UserPrincipal getOAuth2UserPrincipal() {
//        OAuth2UserPrincipal savedPrincipal = null;
//
//        // TODO SecurityContextHolder 외에 Authentication 을 가져올 방법이 없을까?
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null) {
//            Object principal = authentication.getPrincipal();
//            if (principal != null && principal.getClass() == OAuth2UserPrincipal.class) {
//                savedPrincipal = (OAuth2UserPrincipal) principal;
//            }
//        }
//        return savedPrincipal;
//    }

//    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
//
//        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
//        logger.info("{}에서 로그인 완료", provider);
//
//        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
//        logger.info("- {}({})", oAuth2UserInfo.getName(), oAuth2UserInfo.getSid());
//
//        OAuth2UserPrincipal savedPrincipal = getOAuth2UserPrincipal();
//        Long seqUser = null;
//        if (savedPrincipal != null) {
//            seqUser = savedPrincipal.getSeqUser();
//        }
//
//        UserProfileVO savedUserProfile;
//        try {
//            savedUserProfile = userProfileManager.findUserProfile(provider, oAuth2UserInfo.getSid());
//            logger.info("기존회원[{}], 저장 시도", seqUser);
//            savedUserProfile = updateUser(savedUserProfile.getSeqUserProfile(), oAuth2UserInfo);
//            seqUser = savedUserProfile.getUser().getSeqUser();
//            /*
//                TODO 조회된 UserProfile.UserInfo.seqUser 와 Principal.seqUser 를 비교해야한다
//                 -> 이미 user 가 생성된 userProfile 을 나중에 연결하려는 케이스
//                 -> 우선은 Principal 을 갱신하도록함
//             */
//            savedPrincipal = null;
//            logger.info("기존회원, 저장 완료: {}({}) -> {}", savedUserProfile.getName(), savedUserProfile.getSid(), savedUserProfile.getSeqUserProfile());
//        } catch (ErrorResp errorResp) {
//            // TODO NotFoundException 혹은 return null 이 좀더 명확해 보인다. 고민해보자.
//            logger.info("신규회원, 저장 시도");
//            savedUserProfile = createUser(seqUser, provider, oAuth2UserInfo);
//            seqUser = savedUserProfile.getUser().getSeqUser();
//            logger.info("신규회원, 저장 완료: {}({}) -> {}", savedUserProfile.getName(), savedUserProfile.getSid(), savedUserProfile.getSeqUserProfile());
//        }
//
//        if (savedPrincipal != null) {
//            logger.debug("Exist Principal:");
//            logger.debug("\t [{}] before provider: {}", savedPrincipal.getName(), savedPrincipal.getActiveProvider());
//            savedPrincipal.putOAuth2User(provider, oAuth2User);
//        } else {
//            logger.debug("New Principal");
//            savedPrincipal = new OAuth2UserPrincipal(seqUser, provider, oAuth2User);
//        }
//
//        return savedPrincipal;
//    }

    private UserProfileVO createUser(Long seqUser, String provider, OAuth2UserInfo oAuth2UserInfo) {

        UserVO savedUser;
        if (seqUser == null) {
            logger.info("신규 유저 정보 생성");
            UserAdd userAdd = new UserAdd();
            userAdd.setName(oAuth2UserInfo.getName());
            userAdd.setEmail(oAuth2UserInfo.getEmail());
            userAdd.setUrlPicture(oAuth2UserInfo.getUrlPicture());
            savedUser = userManager.addUser(userAdd);
            logger.info("신규 유저 정보 생성 완료: [{}] {}", savedUser.getSeqUser(), savedUser.getName());
        } else {
            logger.info("기존 유저 정보 조회");
            savedUser = userManager.findUser(seqUser);
            logger.info("기존 유저 정보 조회 완료: [{}] {}", savedUser.getSeqUser(), savedUser.getName());
        }
        
        UserProfileAdd userProfileAdd = new UserProfileAdd();
        userProfileAdd.setProvider(provider);
        userProfileAdd.setSid(oAuth2UserInfo.getSid());
        userProfileAdd.setUid(oAuth2UserInfo.getUid());

        userProfileAdd.setName(oAuth2UserInfo.getName());
        userProfileAdd.setEmail(oAuth2UserInfo.getEmail());
        userProfileAdd.setUrlPicture(oAuth2UserInfo.getUrlPicture());

        userProfileAdd.setSeqUser(savedUser.getSeqUser());
        return userProfileManager.addUserProfile(userProfileAdd);
    }

    private UserProfileVO updateUser(Long seqUserProfile, OAuth2UserInfo oAuth2UserInfo) {
        
        UserProfileModify userProfileModify = new UserProfileModify();
        userProfileModify.setName(oAuth2UserInfo.getName());
        userProfileModify.setEmail(oAuth2UserInfo.getEmail());
        userProfileModify.setUrlPicture(oAuth2UserInfo.getUrlPicture());

        return userProfileManager.modifyUserProfile(seqUserProfile, userProfileModify);
    }

}
