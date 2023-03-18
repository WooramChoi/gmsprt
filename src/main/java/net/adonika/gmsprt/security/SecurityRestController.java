package net.adonika.gmsprt.security;

import net.adonika.gmsprt.domain.AuthTokenId;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.security.model.AuthTokenAuthentication;
import net.adonika.gmsprt.security.model.SecurityVO;
import net.adonika.gmsprt.security.service.AuthTokenManager;
import net.adonika.gmsprt.user.service.UserManager;
import net.adonika.gmsprt.user.service.UserProfileManager;
import net.adonika.gmsprt.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/*
    NOTE SecurityController 지만, 메인 인증방식(oAuth2)에 종속된 내용이 많다.
 */
@RestController
@RequestMapping(value = {"/security"})
public class SecurityRestController {

    private final Logger logger = LoggerFactory.getLogger(SecurityRestController.class);
    
    private final AuthTokenManager authTokenManager;
    private final UserManager userManager;
    private final UserProfileManager userProfileManager;
    
    public SecurityRestController(AuthTokenManager authTokenManager,
            UserManager userManager,
            UserProfileManager userProfileManager) {
        
        this.authTokenManager = authTokenManager;
        this.userManager = userManager;
        this.userProfileManager = userProfileManager;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<SecurityVO> securityDetails() {

        AuthTokenAuthentication authentication = (AuthTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        UserProfileVO userProfileVO = authentication.getPrincipal();
        UserVO userVO = userProfileVO.getUser();

        // set securityVO
        SecurityVO securityVO = new SecurityVO(userVO);
        userProfileManager.findUserProfile(userVO.getSeqUser()).forEach(securityVO::putProfile);

        return ResponseEntity.ok(securityVO);
    }

    /**
     * RegistrationId 와 AccessToken 을 사용하여 각 Provider 에게 사용자 정보를 확인, User 및 UserProfile 생성
     * 로그인 역할을 겸함
     * @return UserProfileVO
     */
    @GetMapping(value = {"/login"})
    public ResponseEntity<SecurityVO> login() {

        /*
            NOTE @AuthenticationPrincipal 을 이용할 경우, Authentication.getPrincipal 이 끌려온다.
            그래서 SecurityContextHolder 를 이용하는 중인데... 다른 방법이 없을까?
         */
        AuthTokenAuthentication authentication = (AuthTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        UserProfileVO userProfileVO;
        UserVO userVO;

        if (!authentication.isAuthenticated()) {
            // NOTE saveAuthToken 은 Optional.of 로 반환하기때문에 null 일 수 없음.
            UserProfileVO savedUserProfile = authTokenManager.saveAuthToken(authentication.getDetails()).orElseThrow(ErrorResp::getInternalServerError);
            userVO = savedUserProfile.getUser();
            if (userVO == null) {

                // 신규 user 생성
                UserAdd userAdd = new UserAdd();
                userAdd.setName(savedUserProfile.getName());
                userAdd.setEmail(savedUserProfile.getEmail());
                userAdd.setUrlPicture(savedUserProfile.getUrlPicture());
                userVO = userManager.addUser(userAdd);

                // userProfile 에 연결
                UserProfileModify userProfileModify = new UserProfileModify();
                userProfileModify.setSeqUser(userVO.getSeqUser());
                userProfileVO = userProfileManager.modifyUserProfile(savedUserProfile.getSeqUserProfile(), userProfileModify);
            }
        } else {
            userVO = authentication.getPrincipal().getUser();
        }

        // set securityVO
        SecurityVO securityVO = new SecurityVO(userVO);
        userProfileManager.findUserProfile(userVO.getSeqUser()).forEach(securityVO::putProfile);
        
        return ResponseEntity.ok(securityVO);
    }

    /**
     * 새로운 userProfile 을 추가
     * NOTE 기존 userProfile 을 로그인된 user 에게 빼앗기는 문제가 발생할 수 있다
     * @param registrationId Provider
     * @param accessToken Access Token
     * @return UserProfileVO
     */
    @PostMapping(value = {"/profiles"})
    public ResponseEntity<UserProfileVO> userProfileAdd(@RequestParam String registrationId, @RequestParam String accessToken) {

        AuthTokenAuthentication authentication = (AuthTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Long seqUser = Long.valueOf(authentication.getName());

        UserProfileVO savedUserProfile = authTokenManager.saveAuthToken(new AuthTokenId(registrationId, accessToken)).orElseThrow(ErrorResp::getInternalServerError);

        // userProfile 에 연결
        UserProfileModify userProfileModify = new UserProfileModify();
        userProfileModify.setSeqUser(seqUser);
        UserProfileVO userProfileVO = userProfileManager.modifyUserProfile(savedUserProfile.getSeqUserProfile(), userProfileModify);

        return ResponseEntity.ok(userProfileVO);
    }

}
