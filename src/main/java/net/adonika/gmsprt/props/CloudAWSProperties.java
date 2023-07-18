package net.adonika.gmsprt.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Profile;

@Profile("storage-s3")
@ConfigurationProperties(prefix = "cloud.aws")
@ConstructorBinding
public class CloudAWSProperties {
    
    private final String region;
    private final String bucketName;
    private final String output;
    
    public CloudAWSProperties(String region, String bucketName, String output) {
        this.region = region;
        this.bucketName = bucketName;
        this.output = output;
    }
    
    public String getRegion() {
        return region;
    }
    public String getBucketName() {
        return bucketName;
    }
    public String getOutput() {
        return output;
    }
}
