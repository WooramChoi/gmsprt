package net.adonika.gmsprt.file;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.adonika.gmsprt.file.model.FileAdd;
import net.adonika.gmsprt.file.model.FileDetails;
import net.adonika.gmsprt.file.model.FileResource;
import net.adonika.gmsprt.file.service.FileManager;
import net.adonika.gmsprt.util.SecurityUtil;

@RestController
@RequestMapping(value = {"/files"})
public class FileRestController {
    
    private final Logger logger = LoggerFactory.getLogger(FileRestController.class);
    
    private final FileManager fileManager;
    
    public FileRestController(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    @PostMapping(value = {""}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<FileDetails> fileAdd(@RequestPart(name = "fileInfo") @Valid FileAdd fileAdd, @RequestPart(name = "file") MultipartFile multipartFile) {
        fileAdd.setSeqUser(SecurityUtil.getCurrentSeqUser());
        FileDetails fileDetails = fileManager.addFile(fileAdd, multipartFile);
        return ResponseEntity.created(URI.create("/files/" + fileDetails.getSeqFile())).body(fileDetails);
    }
    
    @GetMapping(value = {"/{seqFile}"})
    public ResponseEntity<FileDetails> fileDetails(@PathVariable Long seqFile) {
        return ResponseEntity.ok(fileManager.findFile(seqFile));
    }
    
    /*
     * NOTE 권장은 /files/{seqFile}/content 라고 한다.
     * 혹은 Accept / Consume 을 활용하여 같은 경로, 다른 리소스를 반환하는 구성도 가능.
     * 우선은 파일명과 확장자가 드러나는 현재의 구조를 유지하고 싶어서 이렇게 구성
     */
    @GetMapping(value = {"/download/{alias}.{ext}"})
    public ResponseEntity<Resource> fileDownload(@PathVariable String alias, @PathVariable String ext) {
        
        Long seqUser = SecurityUtil.getCurrentSeqUser();
        FileResource fileResource = fileManager.findFile(alias, seqUser, null);
        
        ByteArrayResource resource = new ByteArrayResource(fileResource.getBytes());
        MediaType contentType = MediaTypeFactory.getMediaType(fileResource.getFilename()).orElse(MediaType.APPLICATION_OCTET_STREAM);
        
        return ResponseEntity.ok()
                .contentType(contentType)
                .contentLength(fileResource.getSize())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getName() + "\"")
                .body(resource);
    }
    
    @GetMapping(value = {"", "/"})
    public ResponseEntity<Page<String>> fileList(@PageableDefault() Pageable pageable) {
        // TODO
        return ResponseEntity.ok(null);
    }
    
    @DeleteMapping(value = {"/{seqFile}"})
    public ResponseEntity<Long> fileRemove(@PathVariable Long seqBoard) {
        // TODO
        return ResponseEntity.noContent().build();
    }
}
