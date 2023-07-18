package net.adonika.gmsprt.file.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import net.adonika.gmsprt.file.service.StorageManager;

@Profile("storage-linux|storage-windows")
@Service("storageManager")
public class LocalStorageManager implements StorageManager {
    
    public LocalStorageManager() {
    }

    @Override
    public void write(String path, String filename, File file) throws IOException {
        Path filesPath = Paths.get(path).toAbsolutePath().normalize();
        Files.createDirectories(filesPath);
        Files.copy(file.toPath(), filesPath.resolve(filename));
    }

    @Override
    public File read(String path, String filename) throws IOException {
        Path filePath = Paths.get(path).toAbsolutePath().normalize();
        return filePath.resolve(filename).toFile();
    }

    @Override
    public void delete(String path, String filename) throws IOException {
        Path filePath = Paths.get(path).toAbsolutePath().normalize();
        filePath.resolve(filename).toFile().delete();
    }

}
