package net.adonika.gmsprt.board.service.impl;

import net.adonika.gmsprt.board.dao.BoardDao;
import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardSpecificationBuilder;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.user.dao.UserDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("boardManager")
public class BoardManagerImpl implements BoardManager {

    private final BoardDao boardDao;
    private final UserDao userDao;

    public BoardManagerImpl(BoardDao boardDao, UserDao userDao) {
        this.boardDao = boardDao;
        this.userDao = userDao;
    }

    @Override
    public Page<BoardInfo> list(BoardForm boardForm, Pageable pageable) {
        return boardDao.findAll(new BoardSpecificationBuilder(boardForm).build(), pageable);
    }

    @Override
    public BoardInfo create(String subject, String content, Long seqUser) {

        BoardInfo boardInfo = new BoardInfo();
        boardInfo.setTitle(subject);
        boardInfo.setContent(content);

        // TODO MessageSource
        UserInfo userInfo = userDao.findById(seqUser).orElseThrow(() -> new NullPointerException("user not found"));
        boardInfo.setUserInfo(userInfo);

        return boardDao.save(boardInfo);
    }

    @Override
    public BoardInfo create(String subject, String content, String name, String pwd) {

        BoardInfo boardInfo = new BoardInfo();
        boardInfo.setTitle(subject);
        boardInfo.setContent(content);

        boardInfo.setName(name);
        boardInfo.setPwd(pwd);  //TODO 암호화

        return boardDao.save(boardInfo);
    }
}
