package net.adonika.gmsprt;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import net.adonika.gmsprt.config.AppProperties;

@SpringBootTest
class GmsprtApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(GmsprtApplicationTests.class);

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private AppProperties appProperties;

    @Test
    void checkMessageSource() {
        String message_null = messageSource.getMessage("", null, Locale.getDefault());
        Assertions.assertEquals(message_null, "");
        String message_en = messageSource.getMessage("exception.bad_request", null, Locale.US);
        logger.info(message_en);
        Assertions.assertEquals(message_en, "Bad Request.");
        String message_ko_2 = messageSource.getMessage("validation.is_null", new String[]{"boardInfo.seqBoard"}, Locale.getDefault());
        logger.info(message_ko_2);
        Assertions.assertEquals(message_ko_2, "boardInfo.seqBoard은(는) 비어있어야 합니다.");
    }
    
    @Test
    void checkPropertiesAndFile() {
        String pathFile = appProperties.getPathFile();
        logger.info("pathFile: {}", pathFile);
        Assertions.assertNotNull(pathFile);
        
        logger.info("separator: {}", File.separator);
        Path path = Paths.get(pathFile, "202306", "14");
        logger.info("replaced paht: {}", path.toString());
        
        File file = new File(pathFile);
        Assertions.assertTrue(file.isDirectory());
    }

}
