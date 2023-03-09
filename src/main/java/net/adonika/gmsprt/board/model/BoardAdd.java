package net.adonika.gmsprt.board.model;

import javax.validation.constraints.NotBlank;

public class BoardAdd {
    
    /*
     * TODO 조건형 검증
     * pwd가 null이 아닐 경우 형식 체크
     */

    @NotBlank
    private String title;

    //@NotBlank(message = "{validation.board.test}") // messageSource 에 접근 가능
    @NotBlank
    private String content;

    private String name;

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
