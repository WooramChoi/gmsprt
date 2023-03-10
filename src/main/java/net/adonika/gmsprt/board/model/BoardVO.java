package net.adonika.gmsprt.board.model;

import java.util.Objects;

import net.adonika.gmsprt.comm.model.CommVO;
import net.adonika.gmsprt.user.model.UserVO;

public class BoardVO extends CommVO {
    private Long seqBoard;
    private String title;
    private String content;
    private Boolean use;
    private String name;

    private UserVO user;
    
    @Override
    public int hashCode() {
        return Objects.hash(seqBoard);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoardVO other = (BoardVO) obj;
        return Objects.equals(seqBoard, other.seqBoard);
    }

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

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }
}
