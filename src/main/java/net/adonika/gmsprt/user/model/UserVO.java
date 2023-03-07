package net.adonika.gmsprt.user.model;

import java.util.Objects;

import net.adonika.gmsprt.comm.model.CommVO;

public class UserVO extends CommVO {
    private Long seqUser;
    private String name;
    private String email;
    private String urlPicture;

    @Override
    public int hashCode() {
        return Objects.hash(seqUser);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserVO other = (UserVO) obj;
        return Objects.equals(seqUser, other.seqUser);
    }

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