package net.adonika.gmsprt.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import net.adonika.gmsprt.comm.service.CommAuthManager;
import net.adonika.gmsprt.domain.FileInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.file.dao.FileDao;
import net.adonika.gmsprt.file.model.FileAdd;
import net.adonika.gmsprt.file.model.FileDetails;
import net.adonika.gmsprt.file.model.FileModify;
import net.adonika.gmsprt.file.model.FileResource;
import net.adonika.gmsprt.file.model.FileSearch;
import net.adonika.gmsprt.file.service.FileManager;
import net.adonika.gmsprt.file.service.StorageManager;
import net.adonika.gmsprt.props.StorageProperties;
import net.adonika.gmsprt.util.RandomUtil;
import net.adonika.gmsprt.util.RandomUtil.Mode;

@Service("fileManager")
public class FileManagerImpl implements FileManager {
    
    private final Logger logger = LoggerFactory.getLogger(FileManagerImpl.class);
    
    private final FileDao fileDao;
    private final CommAuthManager commAuthManager;
    private final StorageManager storageManager;
    private final StorageProperties storageProperties;
    private final MessageSource messageSource;
    
    public FileManagerImpl(FileDao fileDao, CommAuthManager commAuthManager, StorageManager storageManager, StorageProperties storageProperties, MessageSource messageSource) {
        this.fileDao = fileDao;
        this.commAuthManager = commAuthManager;
        this.storageManager = storageManager;
        this.storageProperties = storageProperties;
        this.messageSource = messageSource;
    }
    
    private <T> T convertTo(FileInfo fileInfo, Class<T> c) {
        logger.debug("[convertTo] fileInfo to {} start".concat(c.getSimpleName()));
        T instance;
        try {
            instance = c.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw ErrorResp.getInternalServerError();
        }
        logger.debug("[convertTo] get new instance of {} done", c.getSimpleName());
        
        BeanUtils.copyProperties(fileInfo, instance);
        
        return instance;
    }

    @Transactional
    @Override
    public FileDetails addFile(FileAdd fileAdd, MultipartFile file) {
        logger.info("[addFile] start");
        FileInfo fileInfo = new FileInfo();
        BeanUtils.copyProperties(fileAdd, fileInfo);
        
        String path = storageProperties.getPath();
        if (!StringUtils.hasText(path)) {
            ErrorResp errorResp = ErrorResp.getInternalServerError();
            errorResp.addError("storage.path", path, messageSource.getMessage("exception.setting.not_enough", null, Locale.getDefault()));
            throw errorResp;
        }
        fileInfo.setPath(path);
        logger.info("[addFile] - path: {}", fileInfo.getPath());
        
        int cntSafe = 10;   // while 문 안전 카운터
        String alias;
        do {
            alias = RandomUtil.getRandomString(10, true, Mode.LOWER);
            cntSafe--;
            if (cntSafe < 0) {
                logger.error("Failed to create Alias");
                throw ErrorResp.getInternalServerError();
            }
        } while(fileDao.findByAlias(alias).isPresent());
        
        String orgName = file.getOriginalFilename();
        String name = StringUtils.getFilename(orgName);
        String ext = StringUtils.getFilenameExtension(orgName);
        fileInfo.setAlias(alias);
        fileInfo.setName(name);
        fileInfo.setExt(ext);
        fileInfo.setSize(file.getSize());
        logger.info("[addFile] - alias: {}", fileInfo.getAlias());
        logger.info("[addFile] - name: {}", fileInfo.getName());
        logger.info("[addFile] - ext: {}", fileInfo.getExt());
        logger.info("[addFile] - size: {}", fileInfo.getSize());
        
        // DB 데이터 생성 및 조회부터 체크하고, 파일 쓰기를 실행한다. Transaction 수행 목적.
        FileInfo savedFileInfo = fileDao.save(fileInfo);
        FileDetails fileDetails = convertTo(savedFileInfo, FileDetails.class);
        
        logger.info("[addFile] done(save): seqFile = {}", savedFileInfo.getSeqFile());
        // File temp = new File(orgName);
        File temp = Paths.get(System.getProperty("user.home")).resolve(orgName).toFile();
        try {
            file.transferTo(temp);
            storageManager.write(savedFileInfo.getPath(), fileDetails.getFilename(), temp);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            throw ErrorResp.getInternalServerError();
        } finally {
            if (temp != null && temp.exists()) {
                temp.delete();
            }
        }
        logger.info("[addFile] done(write): {}{}", savedFileInfo.getPath(), fileDetails.getFilename());
        
        return fileDetails;
    }
    
    @Transactional
    @Override
    public FileDetails modifyFile(Long seqFile, FileModify fileModify) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Transactional
    @Override
    public void removeFile(Long seqFile) {
        logger.info("[removeFile] start: seqFile = {}", seqFile);
        FileInfo savedFileInfo = fileDao.findById(seqFile).orElseThrow(() -> {
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqFile", seqFile, messageSource.getMessage("validation.file.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        String filename = savedFileInfo.getAlias();
        if(StringUtils.hasText(savedFileInfo.getExt())) {
            filename += "." + savedFileInfo.getExt();
        }
        logger.info("[removeFile] delete on storage: {}", filename);
        try {
            storageManager.delete(savedFileInfo.getPath(), filename);
        } catch (IOException e) {
            e.printStackTrace();
            throw ErrorResp.getInternalServerError();
        }
        logger.info("[removeFile] delete on storage done");
        fileDao.delete(savedFileInfo);
        logger.info("[removeFile] done: seqFile = {}", seqFile);
    }

    @Transactional
    @Override
    public FileDetails findFile(Long seqFile) {
        return findFile(seqFile, FileDetails.class);
    }
    
    @Transactional
    @Override
    public <T> T findFile(Long seqFile, Class<T> c) {
        logger.info("[findFile] start: seqFile = {}", seqFile);
        FileInfo savedFileInfo = fileDao.findById(seqFile).orElseThrow(() -> {
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqFile", seqFile, messageSource.getMessage("validation.file.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        logger.info("[findFile] done: seqFile = {}", seqFile);
        return convertTo(savedFileInfo, c);
    }
    
    @Transactional
    @Override
    public List<FileDetails> findFile(String refTable, Long refSeq) {
        logger.info("[findFile] start: refTable = {} / refSeq = {}");
        List<FileInfo> savedFileInfoes = fileDao.findByRefTableAndRefSeq(refTable, refSeq);
        logger.info("[findFile] done: savedFileInfoes[{}]", savedFileInfoes.size());
        List<FileDetails> list = new ArrayList<>();
        savedFileInfoes.forEach(fileInfo -> list.add(convertTo(fileInfo, FileDetails.class)));
        return list;
    }

    @Transactional
    @Override
    public Page<FileDetails> findFile(FileSearch fileSearch) {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    @Override
    public FileResource findFile(String alias, Long seqUser, Long seqGroup) {
        logger.info("[findFile] start: alias = {} / Access User:Group = {}:{}", alias, seqUser, seqGroup);
        FileInfo savedFileInfo = fileDao.findByAliasAndUse(alias, true).orElseThrow(() -> {
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("alias", alias, messageSource.getMessage("validation.file.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        
        if (!commAuthManager.isReadable(savedFileInfo, seqGroup, seqUser)) {
            ErrorResp errorResp = ErrorResp.getForbidden();
            errorResp.addError("seqGroup", seqGroup, messageSource.getMessage("validation.file.read_denied", null, Locale.getDefault()));
            errorResp.addError("seqUser", seqUser, messageSource.getMessage("validation.file.read_denied", null, Locale.getDefault()));
            throw errorResp;
        }
        
        logger.info("[findFile] done: seqFile = {} / alias = {}", savedFileInfo.getSeqFile(), savedFileInfo.getAlias());
        FileResource fileResource = convertTo(savedFileInfo, FileResource.class);
        try {
            File file = storageManager.read(savedFileInfo.getPath(), fileResource.getFilename());
            fileResource.setBytes(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            ErrorResp errorResp = ErrorResp.getInternalServerError();
            throw errorResp;
        }
        return fileResource;
    }

}
