package net.adonika.gmsprt.user.model;

import net.adonika.gmsprt.comm.model.CommModify;

public class UserProfileModify extends CommModify {

    private String uid;

    private String name;

    private String email;

    private String urlPicture;
    
    private Long seqUser;

    public UserProfileModify() {
        super.initIgnores(this.getClass());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        super.setChanges("uid");
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        super.setChanges("name");
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        super.setChanges("email");
        this.email = email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        super.setChanges("urlPicture");
        this.urlPicture = urlPicture;
    }

    public Long getSeqUser() {
        return seqUser;
    }

    public void setSeqUser(Long seqUser) {
        super.setChanges("seqUser");
        this.seqUser = seqUser;
    }
}
