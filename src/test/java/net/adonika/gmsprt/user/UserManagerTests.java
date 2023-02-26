package net.adonika.gmsprt.user;

import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.domain.UserProfileInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserManagerTests {

    private final Logger logger = LoggerFactory.getLogger(UserManagerTests.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserProfileManager userProfileManager;

    @Test
    void create(){

        UserInfo savedUser = userManager.create(
                "Wooram Choi", "dnfka4042@gmail.com",
                "https://lh3.googleusercontent.com/a-/AOh14Ggq5xpJ7amOLyLtL_CXkfftVcFrdKNv_o-MBqF32w"
        );
        Assertions.assertNotNull(savedUser.getSeqUser());
        logger.info("Save User : {}", savedUser.getSeqUser());

        UserProfileInfo savedUserProfile = userProfileManager.create(
                "google", "102693227186529279417", "102693227186529279417",
                savedUser.getName(), savedUser.getEmail(), savedUser.getUrlPicture(),
                savedUser.getSeqUser()
        );

        Assertions.assertNotNull(savedUserProfile.getSeqUserProfile());
        logger.info("Save User Profile: {} ", savedUserProfile.getSeqUserProfile());
        Assertions.assertNotNull(savedUserProfile.getUserInfo());
        Assertions.assertNotNull(savedUserProfile.getUserInfo().getSeqUser());
        logger.info("Seq User in User Profile: {}", savedUserProfile.getUserInfo().getSeqUser());

        Assertions.assertNotNull(savedUser.getDtCreate());
        logger.info("dt_create: {}", savedUser.getDtCreate());
    }
}
