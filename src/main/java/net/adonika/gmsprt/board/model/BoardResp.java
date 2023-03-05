package net.adonika.gmsprt.board.model;

import net.adonika.gmsprt.comm.model.CommResp;
import net.adonika.gmsprt.user.model.UserResp;

public class BoardResp extends CommResp {
    private Long seqBoard;
    private String title;
    private String content;
    private String name;

    private UserResp user;

    public Long getSeqBoard() {
        return seqBoard;
    }

    public void setSeqBoard(Long seqBoard) {
        this.seqBoard = seqBoard;
    }

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

    public UserResp getUser() {
        return user;
    }

    public void setUser(UserResp user) {
        this.user = user;
    }
}
