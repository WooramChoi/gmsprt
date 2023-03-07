package net.adonika.gmsprt.user.model;

import net.adonika.gmsprt.comm.model.CommModify;

public class UserModify extends CommModify{
    
    private String name;
    
    private String email;
    
    private String urlPicture;
    
    public UserModify() {
        super.initIgnores(this.getClass());
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
