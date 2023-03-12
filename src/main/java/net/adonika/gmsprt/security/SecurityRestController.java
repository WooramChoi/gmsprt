package net.adonika.gmsprt.security;

import net.adonika.gmsprt.security.model.OAuth2UserPrincipal;
import net.adonika.gmsprt.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    NOTE SecurityController 지만, 메인 인증방식(oAuth2)에 종속된 내용이 많다.
 */
@RestController
@RequestMapping(value = {"/security"})
public class SecurityRestController {

    private final Logger logger = LoggerFactory.getLogger(SecurityRestController.class);

    private final OAuth2AuthorizedClientService clientService;

    public SecurityRestController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = {"", "/"})
    public ResponseEntity<OAuth2UserPrincipal> authenticatedPrincipalDetails(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {
        return ResponseEntity.ok(oAuth2UserPrincipal);
    }

    @GetMapping(value = {"/providers", "/providers/"})
    public ResponseEntity<List<String>> providerList(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {
        return ResponseEntity.ok(oAuth2UserPrincipal.getProviders());
    }

    @GetMapping(value = {"/providers/{provider}"})
    public ResponseEntity<Map<String, Object>> providerDetails(@PathVariable String provider, @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {

        logger.info("find provider: {}", provider);
        String activeProvider = oAuth2UserPrincipal.getActiveProvider();
        List<String> providers = oAuth2UserPrincipal.getProviders();
        if (!providers.contains(provider)) {
            logger.info("not found [{}] / response active provider [{}]", provider, activeProvider);
            provider = activeProvider;
        }

        oAuth2UserPrincipal.setActiveProvider(provider);
        HashMap<String, Object> providerDetail = new HashMap<>();
        providerDetail.put("name", oAuth2UserPrincipal.getName(provider));
        providerDetail.put("attributes", oAuth2UserPrincipal.getAttributes());
        providerDetail.put("authorities", oAuth2UserPrincipal.getAuthorities());

        oAuth2UserPrincipal.setActiveProvider(activeProvider);
        return ResponseEntity.ok(providerDetail);
    }

    @GetMapping(value = {"/providers/{provider}/client"})
    public ResponseEntity<OAuth2AuthorizedClient> authorizedClientDetails(@PathVariable String provider, @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {

        logger.info("find provider: {}", provider);
        String activeProvider = oAuth2UserPrincipal.getActiveProvider();
        List<String> providers = oAuth2UserPrincipal.getProviders();
        if (!providers.contains(provider)) {
            logger.info("not found [{}] / response active provider [{}]", provider, activeProvider);
            provider = activeProvider;
        }

        /*
            TODO OAuth2AuthorizedClientService 수동 등록 하기
            디폴트로 InMemoryOAuth2AuthorizedClientService, InMemoryClientRegistrationRepository
            가 등록되어있음에도, loadAuthorizedClient 에서 조회가 안됨.
            당장 client 를 써서 조회해야하는건 아니지만, 문제 해결은 필요해 보인다.
         */
        String name = oAuth2UserPrincipal.getName(provider);
        logger.debug("find client: [{}] {} at {}", provider, name, clientService.getClass());
        OAuth2AuthorizedClient authorizedClient = clientService.loadAuthorizedClient(provider, name);

        logger.debug("authorizedClient: {}", ObjectUtil.toJson(authorizedClient));

        return ResponseEntity.ok(authorizedClient);
    }
}
