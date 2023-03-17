package net.adonika.gmsprt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import net.adonika.gmsprt.exception.ErrorResp;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {
    
    @Bean(name = "googleApiClient")
    public WebClient googleApiClient() {
        return WebClient.builder()
                .baseUrl("https://www.googleapis.com")
                .filter(errorHandler())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("Content-Type", "application/x-www-form-urlencoded");
                })
                .build();
    }
    
    @Bean(name = "githubApiClient")
    public WebClient githubApiClient() {
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .filter(errorHandler())
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("Content-Type", "application/x-www-form-urlencoded");
                    httpHeaders.set("Accept", "application/vnd.github+json");
                    httpHeaders.set("X-GitHub-Api-Version", "2022-11-28");
                })
                .build();
    }
    
    private ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is2xxSuccessful()) {
                return Mono.just(clientResponse);
            } else {
                return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                     return Mono.error(ErrorResp.getInstance(clientResponse.statusCode(), errorBody));
                });
            }
        });
    }

}
