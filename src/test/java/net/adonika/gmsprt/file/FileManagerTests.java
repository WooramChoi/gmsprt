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

import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.file.model.FileAdd;
import net.adonika.gmsprt.file.model.FileDetails;
import net.adonika.gmsprt.file.model.FileResource;
import net.adonika.gmsprt.file.service.FileManager;

@SpringBootTest(properties = {"spring.config.location=classpath:application.properties"})
public class FileManagerTests {
    
    private final Logger logger = LoggerFactory.getLogger(FileManagerTests.class);
    
    @Autowired
    private FileManager fileManager;
    
    @Test
    void writeReadDeleteFile() {
        
        FileAdd fileAdd = new FileAdd();
        fileAdd.setRefTable("BOARD_INFO");
        
        String writerData = "str1,str2,str3,str4";
        byte[] content = writerData.getBytes(StandardCharsets.UTF_8);
        MultipartFile multipartFile = new MockMultipartFile("files", "test.csv", "text/plain", content);
        
        FileDetails savedFile = fileManager.addFile(fileAdd, multipartFile);
        Assertions.assertNotNull(savedFile.getSeqFile());
        
        Long seqFile = savedFile.getSeqFile();
        String alias = savedFile.getAlias();
        FileResource fileResource = fileManager.findFile(alias, null, null);
        
        Assertions.assertArrayEquals(content, fileResource.getBytes());
        
        fileManager.removeFile(seqFile);
        Assertions.assertThrows(ErrorResp.getNotFound().getClass(), ()->{fileManager.findFile(seqFile);});
    }

}
