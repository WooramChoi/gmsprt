package net.adonika.gmsprt.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.BasicAWSCredentials;

@Profile("storage-s3")
@ConfigurationProperties(prefix = "cloud.aws.credentials")
@ConstructorBinding
public class CloudAWSCredentialsProperties {
    
    private final BasicAWSCredentials credentials;
    
    public CloudAWSCredentialsProperties(String accessKey, String secretKey) {
        this.credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    public BasicAWSCredentials getCredentials() {
        return credentials;
    }

}
