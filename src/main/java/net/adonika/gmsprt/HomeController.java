package net.adonika.gmsprt;

import net.adonika.gmsprt.security.model.OAuth2UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final OAuth2AuthorizedClientService clientService;

    public HomeController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = {"/search/me"})
    public ResponseEntity<OAuth2AuthorizedClient> searchMe(@AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal) {

        logger.info("seqUser: {}", oAuth2UserPrincipal.getName());

        String provider = oAuth2UserPrincipal.getActiveProvider();
        String name = oAuth2UserPrincipal.getName(provider);

        logger.info("Actived profile: [{}] {}", provider, name);

        OAuth2AuthorizedClient authorizedClient = clientService.loadAuthorizedClient(provider, name);

        return ResponseEntity.ok(authorizedClient);
    }
}
