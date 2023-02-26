package net.adonika.gmsprt;

import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.util.ObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class GmsprtApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(GmsprtApplicationTests.class);

    @Test
    void contextLoads() {

        List<String> ignores =  ObjectUtil.getFieldNames(UserProfileInfo.class);
        logger.info("total {} ", ignores.size());
        ignores.removeAll(Arrays.asList("name", "email", "urlPicture"));
        logger.info("after remove {}", ignores.size());

        Assertions.assertFalse(ignores.contains("name"));
        Assertions.assertFalse(ignores.contains("email"));
        Assertions.assertFalse(ignores.contains("urlPicture"));
    }

}
