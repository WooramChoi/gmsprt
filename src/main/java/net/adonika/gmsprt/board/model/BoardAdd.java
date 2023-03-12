package net.adonika.gmsprt.board.model;

import net.adonika.gmsprt.validation.annotation.NullOrNotBlank;

import javax.validation.constraints.NotBlank;

public class BoardAdd {
    
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private Long seqUser;

    @NullOrNotBlank
    private String name;

    @NullOrNotBlank
    private String pwd;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
