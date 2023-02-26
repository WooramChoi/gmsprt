package net.adonika.gmsprt.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

/**
 * DB 유저 정보 + oAuth2 인증 정보
 */
public class OAuth2UserPrincipal implements OAuth2User {

    private Long seqUser;   // Principal 에 Entity 를 담고 다니기엔 부담스럽기에, key 값만 넣어둠
    private String activeProvider;
    private Map<String, OAuth2User> oAuth2Users;

    public OAuth2UserPrincipal(Long seqUser, String provider, OAuth2User oAuth2User) {
        this.seqUser = seqUser;
        this.activeProvider = provider;

        oAuth2Users = new HashMap<>();
        oAuth2Users.put(provider, oAuth2User);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Users.get(activeProvider).getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2Users.get(activeProvider).getAuthorities();
    }

    @Override
    public String getName() {
        return seqUser.toString();
    }

    public String getName(String provider) {
        return oAuth2Users.get(provider).getName();
    }

    public Long getSeqUser() {
        return seqUser;
    }

    public void setActiveProvider(String provider) {
        activeProvider = provider;
    }

    public String getActiveProvider() {
        return activeProvider;
    }

    public List<String> getProviders() {
        return new ArrayList<>(oAuth2Users.keySet());
    }

    public void putOAuth2User(String provider, OAuth2User oAuth2User) {
        oAuth2Users.put(provider, oAuth2User);
    }
}
