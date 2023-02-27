package net.adonika.gmsprt.board.service.impl;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.adonika.gmsprt.board.dao.BoardDao;
import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardSpecificationBuilder;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.exception.FieldError;
import net.adonika.gmsprt.user.dao.UserDao;

@Service("boardManager")
public class BoardManagerImpl implements BoardManager {

    private final BoardDao boardDao;
    private final UserDao userDao;
    private final MessageSource messageSource;

    public BoardManagerImpl(BoardDao boardDao, UserDao userDao, MessageSource messageSource) {
        this.boardDao = boardDao;
        this.userDao = userDao;
        this.messageSource = messageSource;
    }

    @Override
    public Page<BoardInfo> list(BoardForm boardForm, Pageable pageable) {
        return boardDao.findAll(new BoardSpecificationBuilder(boardForm).build(), pageable);
    }

    @Override
    public BoardInfo create(BoardInfo boardInfo, Long seqUser) {
    	
    	if(boardInfo.getSeqBoard() != null && boardInfo.getSeqBoard() > 0) {
    		throw ErrorResp.getConflict(
    				new FieldError(
    						"seqBoard", boardInfo.getSeqBoard(),
    						messageSource.getMessage("is_null", new String[]{"boardInfo.seqBoard"}, Locale.getDefault())
    						)
    				);
    	}
        
    	if(seqUser != null) {
    		UserInfo userInfo = userDao.findById(seqUser).orElseThrow(
    				() -> ErrorResp.getNotFound(
    						new FieldError(
    								"seqUser", seqUser,
    								messageSource.getMessage("exception.not_found", null, Locale.getDefault())
    								)
    						)
    				//() -> new NullPointerException("user not found")
    				);
            boardInfo.setUserInfo(userInfo);
    	} else {
    		String pwd = boardInfo.getPwd();	//TODO 암호화
            boardInfo.setPwd(pwd);
    	}

        return boardDao.save(boardInfo);
    }
}
