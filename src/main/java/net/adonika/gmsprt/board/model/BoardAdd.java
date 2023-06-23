package net.adonika.gmsprt.board.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import net.adonika.gmsprt.validation.annotation.NullOrNotBlank;

public class BoardAdd {
    
    @NotBlank
    private String title;

    @NotBlank
    private String content;
    
    @NotBlank
    @Size(max = 4000)
    private String plainText;

    private Long seqUser;

    @NullOrNotBlank
    private String name;

    @NullOrNotBlank
    private String pwd;
    
    private List<Long> seqFile;

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

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
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

    public List<Long> getSeqFile() {
        return seqFile;
    }

    public void setSeqFile(List<Long> seqFile) {
        this.seqFile = seqFile;
    }
}
