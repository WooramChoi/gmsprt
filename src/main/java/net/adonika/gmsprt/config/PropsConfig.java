package net.adonika.gmsprt.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import net.adonika.gmsprt.props.AppProperties;
import net.adonika.gmsprt.props.StorageProperties;

@Configuration
@EnableConfigurationProperties(value = {AppProperties.class, StorageProperties.class})
public class PropsConfig {
}
