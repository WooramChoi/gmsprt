package net.adonika.gmsprt.security;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
public class SecurityManagerTests {
    
    private final Logger logger = LoggerFactory.getLogger(SecurityManagerTests.class);
    
    @Autowired
    @Qualifier("googleApiClient")
    private WebClient googleApiClient;
    
    @Autowired
    @Qualifier("githubApiClient")
    private WebClient githubApiClient;
    
    @Test
    public void verifyGoogle() {
        
        String accessToken = "ya29.a0AVvZVsobxhxuqKNJMpfyfOofCcOBrm0o22x1Lw4srjwzs-GFym0b_g4OEi8RAJHdFz4f-ZkLAVgVVECsbwip9cwNKNRpR6E6eC5A71_7erFh2BDI3C88OxHioQmbwbplUIQ82gbSRXbpUm-NpDqlPom6wOmeaCgYKAcMSARESFQGbdwaIcuwxfWyeMYGKmDSpMH38nw0163";
        
        String response = googleApiClient.get()
        .uri(uriBuilder -> uriBuilder
                .path("/oauth2/v3/userinfo")
                .queryParam("alt", "json")
                .build())
        .headers(httpHeaders -> {
            httpHeaders.setBearerAuth(accessToken);
        })
        .retrieve()
        .bodyToMono(String.class)
        .block();
         
        logger.info(response);
        
    }
    
    @Test
    public void verifyGithub() {
        
        String accessToken = "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";
        
        String response = githubApiClient.get()
        .uri(uriBuilder -> uriBuilder
                .path("/user")
                .build())
        .headers(httpHeaders -> {
            httpHeaders.setBearerAuth(accessToken);
        })
        .retrieve()
        .bodyToMono(String.class)
        .block();
         
        logger.info(response);
        
    }

}
