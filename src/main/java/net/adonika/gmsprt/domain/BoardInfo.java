package net.adonika.gmsprt.domain;

import javax.persistence.*;

import net.adonika.gmsprt.util.BooleanYnConverter;

@Entity
public class BoardInfo extends CommInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ_BOARD", length = 10)
    private Long seqBoard;

    @Column(length = 255)
    private String title;
    
    @Lob
    private String content;
    
    @Column(length = 4000)
    private String plainText;
    
    @Column(name="yn_use", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    @Convert(converter = BooleanYnConverter.class)
    private Boolean use = true;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = UserInfo.class)
    @JoinColumn(name = "SEQ_USER")
    private UserInfo userInfo;

    @Column(length = 50)
    private String name;

    @Column(length = 255)
    private String pwd;

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

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public Boolean getUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
