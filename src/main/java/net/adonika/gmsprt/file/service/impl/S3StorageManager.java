package net.adonika.gmsprt.file.service.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import net.adonika.gmsprt.file.service.StorageManager;

@Service("storageManager")
@Profile("aws")
public class S3StorageManager implements StorageManager {

    @Override
    public void write(String path, String filename, File file) throws IOException {
        
        
        
    }

    @Override
    public File read(String path, String filename) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

}
