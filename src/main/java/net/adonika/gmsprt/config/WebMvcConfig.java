package net.adonika.gmsprt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.adonika.gmsprt.security.HtmlCharacterEscapes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

//        registry.addResourceHandler("/**")
//                .resourceChain(true)
//                .addResolver(new PathResourceResolver(){
//                    @Override
//                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
//                        logger.info("resourcePath: " + resourcePath);
//                        Resource requestedResource = location.createRelative(resourcePath);
//                        if(requestedResource.exists() && requestedResource.isReadable()){
//                            return requestedResource;
//                        }else{
//                            return new ClassPathResource("/static/index.html");
//                        }
//                    }
//                });
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());

        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
