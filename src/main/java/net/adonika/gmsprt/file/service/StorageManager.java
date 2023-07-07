package net.adonika.gmsprt.file.service;

import java.io.File;
import java.io.IOException;

public interface StorageManager {
    
    void write(String path, String filename, File file) throws IOException;
    
    File read(String path, String filename) throws IOException;

}
