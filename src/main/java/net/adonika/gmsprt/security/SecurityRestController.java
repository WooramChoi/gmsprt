package net.adonika.gmsprt.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.adonika.gmsprt.security.model.AuthTokenAuthentication;
import net.adonika.gmsprt.security.service.AuthTokenManager;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.user.UserProfileManager;
import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserProfileModify;
import net.adonika.gmsprt.user.model.UserProfileVO;
import net.adonika.gmsprt.user.model.UserVO;

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

//    @GetMapping(value = {"", "/"})
//    public ResponseEntity<OAuth2UserPrincipal> authenticatedPrincipalD Retails(@AuthenticationPrincipal AuthenticationPrincipal oAuth2UserPrincipal) {
//        return ResponseEntity.ok(oAuth2UserPrincipal);
//    }
    
    
    @GetMapping(value = {"/login"})
    public ResponseEntity<UserProfileVO> login() {
        
        AuthTokenAuthentication authentication = (AuthTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        
        UserProfileVO userProfileVO;
        if (!authentication.isAuthenticated()) {
            // NOTE saveAuthToken 은 Optional.of 로 반환하기때문에 null 일 수 없음.
            UserVO userVO;
            UserProfileVO savedUserProfile = authTokenManager.saveAuthToken(authentication.getDetails()).orElse(null);
            if (savedUserProfile.getUser() != null) {
                // NOTE userProfile 의 정보로 user 를 덮어씌우고 싶으면 구현. 일단 별도로 관리하고싶으니까 구현하지 않는다.
                userProfileVO = savedUserProfile;
            } else {
                
                UserAdd userAdd = new UserAdd();
                userAdd.setName(savedUserProfile.getName());
                userAdd.setEmail(savedUserProfile.getEmail());
                userAdd.setUrlPicture(savedUserProfile.getUrlPicture());
                
                userVO = userManager.addUser(userAdd);
                
                UserProfileModify userProfileModify = new UserProfileModify();
                userProfileModify.setSeqUser(userVO.getSeqUser());
                
                userProfileVO = userProfileManager.modifyUserProfile(savedUserProfile.getSeqUserProfile(), userProfileModify);
            }
        } else {
            userProfileVO = authentication.getPrincipal();
        }
        
        return ResponseEntity.ok(userProfileVO);
    }
    

}
