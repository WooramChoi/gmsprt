package net.adonika.gmsprt.board.model;

import java.util.Objects;

import net.adonika.gmsprt.comm.model.CommDetails;
import net.adonika.gmsprt.user.model.UserDetails;

public class BoardSummary extends CommDetails {
    private Long seqBoard;
    private String title;
    private String contentSummary;
    private Boolean use;
    private String name;

    private UserDetails user;
    
    public static final int LENGTH_SUMMARY = 255;
    
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
        BoardSummary other = (BoardSummary) obj;
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

    public String getContentSummary() {
        return contentSummary;
    }

    public void setContentSummary(String contentSummary) {
        this.contentSummary = contentSummary;
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

    public UserDetails getUser() {
        return user;
    }

    public void setUser(UserDetails user) {
        this.user = user;
    }
}
