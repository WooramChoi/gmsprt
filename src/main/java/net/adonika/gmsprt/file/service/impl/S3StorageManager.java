package net.adonika.gmsprt.file.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import net.adonika.gmsprt.file.service.StorageManager;
import net.adonika.gmsprt.props.CloudAWSCredentialsProperties;
import net.adonika.gmsprt.props.CloudAWSProperties;

@Profile("storage-s3")
@Service("storageManager")
public class S3StorageManager implements StorageManager {
    
    private final Logger logger = LoggerFactory.getLogger(S3StorageManager.class);
    
    private final AmazonS3 s3client;
    private final String bucketName;
    
    public S3StorageManager(CloudAWSProperties cloudAWSProperties, CloudAWSCredentialsProperties credentials) {
        
        logger.info("cloudAWSProperties.getRegion: {}", cloudAWSProperties.getRegion());
        logger.debug("cloudAWSProperties.getBucketName: {}", cloudAWSProperties.getBucketName());
        logger.debug("cloudAWSProperties.getOutput: {}", cloudAWSProperties.getOutput());
        logger.debug("CloudAWSCredentialsProperties.getCredentials");
        logger.debug("- getAWSAccessKeyId: {}", credentials.getCredentials().getAWSAccessKeyId());
        logger.debug("- getAWSSecretKey: {}", credentials.getCredentials().getAWSSecretKey());
        
        s3client = AmazonS3ClientBuilder.standard()
        .withRegion(cloudAWSProperties.getRegion())
        .withCredentials(new AWSStaticCredentialsProvider(credentials.getCredentials()))
        .build();
        
        this.bucketName = cloudAWSProperties.getBucketName();
    }

    @Override
    public void write(String path, String filename, File file) throws IOException {
        try {
            s3client.putObject(bucketName, path + filename, file);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }

    @Override
    public File read(String path, String filename) throws IOException {
        File temp = null;
        try {
            S3Object object = s3client.getObject(bucketName, path + filename);
            S3ObjectInputStream s3is = object.getObjectContent();
            temp = new File(filename);
            temp.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(temp);
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            s3is.close();
            fos.close();
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return temp;
    }

    @Override
    public void delete(String path, String filename) throws IOException {
        try {
            s3client.deleteObject(bucketName, path + filename);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
        }
    }

}
