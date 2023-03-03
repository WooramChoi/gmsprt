package net.adonika.gmsprt.board.model;

import org.springframework.beans.BeanUtils;

import net.adonika.gmsprt.comm.model.CommResp;
import net.adonika.gmsprt.domain.BoardInfo;

public class BoardResp extends CommResp {

    // BoardInfo
    private Long seqBoard;
    private String title;
    private String content;
    private String name;
    private User user;

    // UserInfo
    public class User {
    	private String name;
    	private String email;
        private String urlPicture;
        
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
    
    public BoardResp(BoardInfo boardInfo) {
    	BeanUtils.copyProperties(boardInfo, this, new String[] {"userInfo"});
    	
    	if(boardInfo.getUserInfo() != null) {
    		this.user = new User();
    		BeanUtils.copyProperties(boardInfo.getUserInfo(), this.user);
    	}
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
}
