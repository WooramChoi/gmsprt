package net.adonika.gmsprt.security.model.impl;

import net.adonika.gmsprt.security.model.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    /**
     * { id: '102693227186529279417',
     *   displayName: 'Wooram Choi',
     *   name: { familyName: 'Choi', givenName: 'Wooram' },
     *   emails: [ { value: 'dnfka4042@gmail.com', verified: true } ],
     *   photos:
     *    [ { value:
     *         'https://lh3.googleusercontent.com/a-/AOh14Ggq5xpJ7amOLyLtL_CXkfftVcFrdKNv_o-MBqF32w' } ],
     *   provider: 'google',
     *   _raw:
     *    '{\n  "sub": "102693227186529279417",\n  "name": "Wooram Choi",\n  "given_name": "Wooram",\n  "family_name": "Choi",\n  "picture": "https://lh3.googleusercontent.com/a-/AOh14Ggq5xpJ7amOLyLtL_CXkfftVcFrdKNv_o-MBqF32w",\n  "email": "dnfka4042@gmail.com",\n  "email_verified": true,\n  "locale": "ko"\n}',
     *   _json:
     *    { sub: '102693227186529279417',
     *      name: 'Wooram Choi',
     *      given_name: 'Wooram',
     *      family_name: 'Choi',
     *      picture:
     *       'https://lh3.googleusercontent.com/a-/AOh14Ggq5xpJ7amOLyLtL_CXkfftVcFrdKNv_o-MBqF32w',
     *      email: 'dnfka4042@gmail.com',
     *      email_verified: true,
     *      locale: 'ko' } }
     * @param attributes
     */

    public GoogleOAuth2UserInfo(Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getSid() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getUid() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getUrlPicture() {
        return attributes.get("picture").toString();
    }
}
