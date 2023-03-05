package net.adonika.gmsprt.user.model;

import net.adonika.gmsprt.comm.model.CommResp;

public class UserResp extends CommResp {
    private Long seqUser;
    private String name;
    private String email;
    private String urlPicture;

    public Long getSeqUser() {
        return seqUser;
    }

    public void setSeqUser(Long seqUser) {
        this.seqUser = seqUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}