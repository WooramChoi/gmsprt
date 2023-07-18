package net.adonika.gmsprt.file.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import net.adonika.gmsprt.file.model.FileAdd;
import net.adonika.gmsprt.file.model.FileDetails;
import net.adonika.gmsprt.file.model.FileModify;
import net.adonika.gmsprt.file.model.FileResource;
import net.adonika.gmsprt.file.model.FileSearch;

public interface FileManager {
    
    FileDetails addFile(FileAdd fileAdd, MultipartFile file);
    
    FileDetails modifyFile(Long seqFile, FileModify fileModify);
    
    void removeFile(Long seqFile);
    
    FileDetails findFile(Long seqFile);
    
    <T> T findFile(Long seqFile, Class<T> c);
    
    List<FileDetails> findFile(String refTable, Long refSeq);
    
    Page<FileDetails> findFile(FileSearch fileSearch);
    
    FileResource findFile(String alias, Long seqUser, Long seqGroup);    // TODO Facade Pattern 에 따라, 권한 관리와 파일 관리가 혼합된 서비스를 생성해야함

}
