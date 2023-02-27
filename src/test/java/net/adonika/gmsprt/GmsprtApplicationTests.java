package net.adonika.gmsprt;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import net.adonika.gmsprt.domain.UserProfileInfo;
import net.adonika.gmsprt.util.ObjectUtil;

@SpringBootTest
class GmsprtApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(GmsprtApplicationTests.class);
    
    @Autowired
    private MessageSource messageSource;

    @Test
    void contextLoads() {

        List<String> ignores =  ObjectUtil.getFieldNames(UserProfileInfo.class);
        logger.info("total {} ", ignores.size());
        ignores.removeAll(Arrays.asList("name", "email", "urlPicture"));
        logger.info("after remove {}", ignores.size());

        Assertions.assertFalse(ignores.contains("name"));
        Assertions.assertFalse(ignores.contains("email"));
        Assertions.assertFalse(ignores.contains("urlPicture"));
        
        String message_en = messageSource.getMessage("exception.bad_request", null, Locale.US);
        logger.info(message_en);
        Assertions.assertEquals(message_en, "Bad Request.");
        String message_ko_2 = messageSource.getMessage("is_null", new String[] {"boardInfo.seqBoard"}, Locale.getDefault());
        logger.info(message_ko_2);
        Assertions.assertEquals(message_ko_2, "boardInfo.seqBoard은(는) 비어있어야 합니다.");
    }

}
