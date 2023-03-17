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
        
        String accessToken = "ya29.a0AVvZVsp_b7l3gbE-gaTYCsMTCsM8UhpAz0cmBT9uO2hBOS5S5i_9IyyKHD2z4DHZj4nr0WyczD5bpsn3ltcTpW0TrINTZNfAECql02bgNk4H_X5PTYgIqVhefWwF3KxjV2d9zYY158VDdaI34widzboBjXBs3gaCgYKAVcSARESFQGbdwaIoKLQOORR_YvDajL5tonqaA0165";
        
        String response = googleApiClient.get()
        .uri(uriBuilder -> uriBuilder
                .path("/oauth2/v1/userinfo")
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
