package net.adonika.gmsprt.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import net.adonika.gmsprt.props.CloudAWSCredentialsProperties;
import net.adonika.gmsprt.props.CloudAWSProperties;

@Profile("storage-s3")
@Configuration
@EnableConfigurationProperties(value = {CloudAWSProperties.class, CloudAWSCredentialsProperties.class})
public class AWSPropsConfig {

}
