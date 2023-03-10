package net.adonika.gmsprt.board.model;

import net.adonika.gmsprt.comm.model.CommModify;

public class BoardModify extends CommModify {
    
    /*
     * TODO 조건형 검증
     * pwd가 null이 아닐 경우 형식 체크
     * changePwd=true 일 경우 newPwd 형식 체크
     */

    private String title;

    private String content;
    
    private Boolean use;

    private String name;

    private String pwd;
    
    private boolean changePwd;
    
    private String newPwd;
    
    public BoardModify() {
        super.initIgnores(this.getClass());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        super.removeIgnores("title");
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        super.removeIgnores("content");
        this.content = content;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        super.removeIgnores("use");
        this.use = use;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        super.removeIgnores("name");
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        // super.removeIgnores("pwd");
        this.pwd = pwd;
    }

    public boolean isChangePwd() {
        return changePwd;
    }

    public void setChangePwd(boolean changePwd) {
        this.changePwd = changePwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
    
}
