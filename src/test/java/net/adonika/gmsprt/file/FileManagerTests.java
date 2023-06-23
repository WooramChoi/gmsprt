package net.adonika.gmsprt.file;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import net.adonika.gmsprt.file.model.FileAdd;
import net.adonika.gmsprt.file.model.FileDetails;
import net.adonika.gmsprt.file.service.FileManager;

@SpringBootTest
public class FileManagerTests {
    
    private final Logger logger = LoggerFactory.getLogger(FileManagerTests.class);
    
    @Autowired
    private FileManager fileManager;
    
    @Test
    void writeFile() {
        
        FileAdd fileAdd = new FileAdd();
        fileAdd.setRefTable("BOARD_INFO");
        
        String writerData = "str1,str2,str3,str4";
        MultipartFile multipartFile = new MockMultipartFile("files", "test.csv", "text/plain", writerData.getBytes(StandardCharsets.UTF_8));
        
        FileDetails savedFile = fileManager.addFile(fileAdd, multipartFile);
        Assertions.assertNotNull(savedFile.getSeqFile());
        
    }

}
