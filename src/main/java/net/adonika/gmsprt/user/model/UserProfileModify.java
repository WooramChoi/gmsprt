package net.adonika.gmsprt.user.model;

import net.adonika.gmsprt.comm.model.CommModify;

public class UserProfileModify extends CommModify {

    private String uid;

    private String name;

    private String email;

    private String urlPicture;
    
    public UserProfileModify() {
        super.initIgnores(this.getClass());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        super.removeIgnores("uid");
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        super.removeIgnores("name");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        super.removeIgnores("email");
        this.email = email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        super.removeIgnores("urlPicture");
        this.urlPicture = urlPicture;
    }
}
