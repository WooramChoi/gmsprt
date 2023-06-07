package net.adonika.gmsprt.board.model;

import javax.validation.constraints.Size;

import net.adonika.gmsprt.comm.model.CommModify;
import net.adonika.gmsprt.validation.annotation.NullOrNotBlank;

//@NotNullAtLeastOne(targets = {"seqUser", "pwd"}, message = "{validation.board.not_null_user_or_pwd}")
public class BoardModify extends CommModify {
    
    @NullOrNotBlank
    private String title;

    @NullOrNotBlank
    private String content;
    
    @NullOrNotBlank
    @Size(max = 4000)
    private String plainText;

    @NullOrNotBlank
    private Boolean use;

    private Long seqUser;

    @NullOrNotBlank
    private String name;

    @NullOrNotBlank
    private String pwd;

    @NullOrNotBlank
    private String newPwd;
    
    public BoardModify() {
        super.initIgnores(this.getClass());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        super.setChanges("title");
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        super.setChanges("content");
        this.content = content;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        super.setChanges("plainText");
        this.plainText = plainText;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        super.setChanges("use");
        this.use = use;
    }

    public Long getSeqUser() {
        return seqUser;
    }

    public void setSeqUser(Long seqUser) {
        // super.setChanges("seqUser");
        this.seqUser = seqUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        super.setChanges("name");
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        // super.removeIgnores("pwd");
        this.pwd = pwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
    
}
