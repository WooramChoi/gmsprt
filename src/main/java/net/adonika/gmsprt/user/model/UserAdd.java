package net.adonika.gmsprt.user.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserAdd {
    
    @NotBlank
    private String name;
    
    @Email
    private String email;
    
    private String urlPicture;

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
