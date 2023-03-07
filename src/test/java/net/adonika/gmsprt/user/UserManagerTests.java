package net.adonika.gmsprt.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserProfileAdd;
import net.adonika.gmsprt.user.model.UserProfileVO;
import net.adonika.gmsprt.user.model.UserVO;

@SpringBootTest
public class UserManagerTests {

    private final Logger logger = LoggerFactory.getLogger(UserManagerTests.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserProfileManager userProfileManager;

    private UserAdd getUserAdd(String name, String email, String urlPicture) {
        UserAdd userAdd = new UserAdd();
        userAdd.setName(name);
        userAdd.setEmail(email);
        userAdd.setUrlPicture(urlPicture);
        return userAdd;
    }

    private UserProfileAdd getUserProfileAdd(String provider, String sid, String uid, String name, String email, String urlPicture) {
        UserProfileAdd userProfileAdd = new UserProfileAdd();
        userProfileAdd.setProvider(provider);
        userProfileAdd.setSid(sid);
        userProfileAdd.setUid(uid);

        userProfileAdd.setName(name);
        userProfileAdd.setEmail(email);
        userProfileAdd.setUrlPicture(urlPicture);
        return userProfileAdd;
    }

    @Test
    void create() {

        UserVO savedUser = userManager.addUser(getUserAdd(
                "Wooram Choi", "dnfka4042@gmail.com",
                "https://lh3.googleusercontent.com/a-/AOh14Ggq5xpJ7amOLyLtL_CXkfftVcFrdKNv_o-MBqF32w")
        );
        Assertions.assertNotNull(savedUser.getSeqUser());
        logger.info("Save User : {}", savedUser.getSeqUser());

        UserProfileVO savedUserProfile = userProfileManager.addUserProfile(getUserProfileAdd(
                        "google", "102693227186529279417", "102693227186529279417",
                        savedUser.getName(), savedUser.getEmail(), savedUser.getUrlPicture()),
                savedUser.getSeqUser()
        );

        Assertions.assertNotNull(savedUserProfile.getSeqUserProfile());
        logger.info("Save User Profile: {} ", savedUserProfile.getSeqUserProfile());
        Assertions.assertNotNull(savedUserProfile.getUser());
        Assertions.assertNotNull(savedUserProfile.getUser().getSeqUser());
        logger.info("Seq User in User Profile: {}", savedUserProfile.getUser().getSeqUser());

        Assertions.assertNotNull(savedUser.getDtCreate());
        logger.info("dt_create: {}", savedUser.getDtCreate());
    }
}
