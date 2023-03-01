package net.adonika.gmsprt.security.model;

import net.adonika.gmsprt.security.model.impl.GithubOAuth2UserInfo;
import net.adonika.gmsprt.security.model.impl.GoogleOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public OAuth2UserInfoFactory() {

    }

    enum AuthProvider {
        google,
        github
    }

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equals(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equals(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new IllegalArgumentException("Invalid Provider: " + registrationId);
        }
    }

}
