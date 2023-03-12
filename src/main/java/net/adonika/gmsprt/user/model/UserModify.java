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
}
