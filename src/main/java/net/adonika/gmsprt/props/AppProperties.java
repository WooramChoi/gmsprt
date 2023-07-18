package net.adonika.gmsprt.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "app")
@ConstructorBinding
public class AppProperties {
    
    public AppProperties() {
    }
    
}
