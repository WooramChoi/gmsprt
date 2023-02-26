package net.adonika.gmsprt.board.model;

public class BoardResp {

    // BoardInfo
    private Long seqBoard;
    private String subject;
    private String content;

    // UserInfo
    private String name;
    private String email;
    private String urlPicture;

    public Long getSeqBoard() {
        return seqBoard;
    }

    public void setSeqBoard(Long seqBoard) {
        this.seqBoard = seqBoard;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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
