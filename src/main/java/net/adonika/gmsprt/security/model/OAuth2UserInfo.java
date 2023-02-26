package net.adonika.gmsprt.security.model;

import org.springframework.util.Assert;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        Assert.notNull(attributes, "Should be not null");
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getSid();

    public abstract String getUid();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getUrlPicture();

}
