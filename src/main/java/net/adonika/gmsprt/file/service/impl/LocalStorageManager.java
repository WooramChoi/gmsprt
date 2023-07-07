package net.adonika.gmsprt.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import net.adonika.gmsprt.config.AppProperties;
import net.adonika.gmsprt.file.service.StorageManager;

@Service("storageManager")
@Profile("!aws")
public class LocalStorageManager implements StorageManager {
    
    private final AppProperties appProperties;
    private final MessageSource messageSource;
    
    public LocalStorageManager(AppProperties appProperties, MessageSource messageSource) {
        this.appProperties = appProperties;
        this.messageSource = messageSource;
    }

    @Override
    public void write(String path, String filename, File file) throws IOException {
        Path filesPath = Paths.get(path).toAbsolutePath().normalize();
        Files.createDirectories(filesPath);
        Files.copy(file.toPath(), filesPath.resolve(filename));
    }

    @Override
    public File read(String path, String filename) throws IOException {
        return new File(path + filename);
    }

}
