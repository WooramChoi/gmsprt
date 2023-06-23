package net.adonika.gmsprt.board.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import net.adonika.gmsprt.board.BoardSpecificationBuilder;
import net.adonika.gmsprt.board.dao.BoardDao;
import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.model.BoardDetails;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardSearch;
import net.adonika.gmsprt.board.model.BoardSummary;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.file.model.FileModify;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.util.ObjectUtil;

@Service("boardManager")
public class BoardManagerImpl implements BoardManager {

    private final Logger logger = LoggerFactory.getLogger(BoardManagerImpl.class);

    private final BoardDao boardDao;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public BoardManagerImpl(BoardDao boardDao, UserDao userDao, PasswordEncoder passwordEncoder, MessageSource messageSource) {
        this.boardDao = boardDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = messageSource;
    }
    
    /**
     * BoardInfo 및 하위 Entity 들을 VO 객체에 맵핑
     * @param boardInfo
     * @param c
     * @param <T>
     * @return
     */
    private <T> T convertTo(BoardInfo boardInfo, Class<T> c) {
        logger.debug("[convertTo] boardInfo to {} start", c.getSimpleName());
        T instance;
        try {
            instance = c.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            throw ErrorResp.getInternalServerError();
        }
        logger.debug("[convertTo] get new instance of {} done", c.getSimpleName());

        BeanUtils.copyProperties(boardInfo, instance, "userInfo");
        logger.debug("[convertTo] copy boardInfo to {} done: {}", c.getSimpleName(), ObjectUtil.toJson(instance));

        UserInfo userInfo = boardInfo.getUserInfo();
        if (userInfo != null) {
            logger.debug("[convertTo] boardInfo has UserInfo: seqUser = {}", userInfo.getSeqUser());
            
            try {
                ObjectUtil.copyToField(instance, "user", userInfo);
                logger.debug("[convertTo] copy userInfo to \"user\" done: {}", ObjectUtil.toJson(instance));
            } catch (NoSuchFieldException e) {
                logger.error("[convertTo] {} hasn't field of \"user\"", c.getSimpleName());
            }
        }

        return instance;
    }
    
    @Transactional
    @Override
    public BoardDetails addBoard(BoardAdd boardAdd) {
        logger.info("[addBoard] start");
        BoardInfo boardInfo = new BoardInfo();
        BeanUtils.copyProperties(boardAdd, boardInfo);

        Long seqUser = boardAdd.getSeqUser();
        if (Optional.ofNullable(seqUser).orElse(0L) > 0L) {
            logger.info("[addBoard] has UserInfo: seqUser = {}", seqUser);
            UserInfo userInfo = userDao.findById(seqUser).orElseThrow(()->{
                ErrorResp errorResp = ErrorResp.getNotFound();
                errorResp.addError("seqUser", seqUser, messageSource.getMessage("validation.user.not_found", null, Locale.getDefault()));
                return errorResp;
            });
            boardInfo.setUserInfo(userInfo);
            logger.info("[addBoard] set UserInfo done");
        }
        
        if (StringUtils.hasText(boardInfo.getPwd())) {
            logger.info("[addBoard] has pwd");
            String pwd = passwordEncoder.encode(boardInfo.getPwd());
            boardInfo.setPwd(pwd);
            logger.info("[addBoard] set pwd done");
        }
        
        BoardInfo savedBoardInfo = boardDao.save(boardInfo);
        logger.info("[addBoard] done: seqBoard = {}", savedBoardInfo.getSeqBoard());
        
        // TODO fileManager.modifyFile -> 올라온 첨부파일들을 일괄 업데이트
        if (boardAdd.getSeqFile() != null) {
            for(Long seqFile: boardAdd.getSeqFile()) {
                FileModify fileModify = new FileModify();
                fileModify.setRefTable("BOARD_INFO");
                fileModify.setRefSeq(savedBoardInfo.getSeqBoard());
            }
        }
        
        return convertTo(savedBoardInfo, BoardDetails.class);
    }

    @Transactional
    @Override
    public BoardDetails modifyBoard(Long seqBoard, BoardModify boardModify) {
        logger.info("[modifyBoard] start: seqBoard = {}", seqBoard);
        BoardInfo boardInfo = boardDao.findById(seqBoard).orElseThrow(()->{
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqBoard", seqBoard, messageSource.getMessage("validation.board.not_found", null, Locale.getDefault()));
            return errorResp;
        });

        /*
            NOTE 권한에 관하여.
            Authentication 에 관련된 내용은 Controller 에 작성했으면 좋겠지만,
            Password 가 Service 밖으로 나가지 않았으면하기에 이곳에 작성.
            boolean canModifyBoard(seqBoard, seqUser, pwd) throws AuthenticationException 서비스를 추가하는 것도 괜찮은 방법 같다.
         */
        logger.info("[modifyBoard] check authorization");
        Long seqUser = boardModify.getSeqUser();
        if (boardInfo.getUserInfo() != null) {
            logger.info("[modifyBoard] check by owner");
            logger.debug("[modifyBoard] owner = {} / accessor = {}", boardInfo.getUserInfo().getSeqUser(), seqUser);
            if (!boardInfo.getUserInfo().getSeqUser().equals(seqUser)) {
                ErrorResp errorResp = ErrorResp.getForbidden();
                errorResp.addError("seqUser", seqUser, messageSource.getMessage("validation.board.not_owner", null, Locale.getDefault()));
                throw errorResp;
            }
        } else {
            if (StringUtils.hasText(boardInfo.getPwd())) {
                logger.info("[modifyBoard] check by pwd");
                if (!StringUtils.hasText(boardModify.getPwd())) {
                    ErrorResp errorResp = ErrorResp.getForbidden();
                    errorResp.addError("pwd", null, messageSource.getMessage("validation.pwd.not_null", null, Locale.getDefault()));
                    throw errorResp;
                } else {
                    if (!passwordEncoder.matches(boardModify.getPwd(), boardInfo.getPwd())) {
                        ErrorResp errorResp = ErrorResp.getForbidden();
                        errorResp.addError("pwd", null, messageSource.getMessage("validation.pwd.incorrent", null, Locale.getDefault()));
                        throw errorResp;
                    }
                }
            }
        }
        logger.info("[modifyBoard] check authorization done");
        
        BeanUtils.copyProperties(boardModify, boardInfo, boardModify.getIgnores());

        if (boardModify.getNewPwd() != null) {
            logger.info("[modifyBoard] change pwd");
            if (!StringUtils.hasText(boardModify.getNewPwd())) {
                boardInfo.setPwd(null);
            } else {
                String pwd = passwordEncoder.encode(boardModify.getNewPwd());
                boardInfo.setPwd(pwd);
            }
            logger.info("[modifyBoard] set new pwd done");
        }
        
        BoardInfo savedBoardInfo = boardDao.saveAndFlush(boardInfo);
        logger.info("[modifyBoard] done: seqBoard = {}", savedBoardInfo.getSeqBoard());
        return convertTo(savedBoardInfo, BoardDetails.class);
    }

    @Transactional
    @Override
    public void removeBoard(Long seqBoard) {
        logger.info("[removeBoard] start: seqBoard = {}", seqBoard);
        boardDao.deleteById(seqBoard);
        logger.info("[removeBoard] done: seqBoard = {}", seqBoard);
    }

    @Transactional
    @Override
    public void removeBoard(BoardSearch boardSearch) {
        logger.info("[removeBoard] start: boardSearch = {}", ObjectUtil.toJson(boardSearch));
        List<BoardInfo> list = boardDao.findAll(new BoardSpecificationBuilder(boardSearch).build());
        // for log
        Long[] ids = new Long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).getSeqBoard();
        }
        boardDao.deleteAllInBatch(list);
        logger.info("[removeBoard] done: ids[{}] = {}", ids.length, ObjectUtil.toJson(ids));
    }

    @Transactional
    @Override
    public BoardDetails findBoard(Long seqBoard) {
        logger.info("[findBoard] start: seqBoard = {}", seqBoard);
        BoardInfo savedBoardInfo = boardDao.findById(seqBoard).orElseThrow(()->{
            ErrorResp errorResp = ErrorResp.getNotFound();
            errorResp.addError("seqBoard", seqBoard, messageSource.getMessage("validation.board.not_found", null, Locale.getDefault()));
            return errorResp;
        });
        logger.info("[findBoard] done: seqBoard = {}", seqBoard);
        return convertTo(savedBoardInfo, BoardDetails.class);
    }

    @Transactional
    @Override
    public List<BoardDetails> findBoard(BoardSearch boardSearch) {
        logger.info("[findBoard] start: boardSearch = {}", ObjectUtil.toJson(boardSearch));
        List<BoardInfo> savedBoardInfos = boardDao.findAll(new BoardSpecificationBuilder(boardSearch).build());
        logger.info("[findBoard] done: savedBoardInfos[{}]", savedBoardInfos.size());
        List<BoardDetails> list = new ArrayList<>();
        savedBoardInfos.forEach(boardInfo->list.add(convertTo(boardInfo, BoardDetails.class)));
        return list;
    }

    @Transactional
    @Override
    public Page<BoardSummary> findBoard(BoardSearch boardSearch, Pageable pageable) {
        logger.info("[findBoard] start: boardSearch = {} / pageable = {}", ObjectUtil.toJson(boardSearch), ObjectUtil.toJson(pageable));
        Page<BoardInfo> savedBoardInfos = boardDao.findAll(new BoardSpecificationBuilder(boardSearch).build(), pageable);
        logger.info("[findBoard] done: savedBoardInfos[{}]", savedBoardInfos.getSize());
        Page<BoardSummary> boardSummaries = savedBoardInfos.map(boardInfo->{
                BoardSummary converted = convertTo(boardInfo, BoardSummary.class);
                String contentSummary = boardInfo.getPlainText();
                if (contentSummary != null && contentSummary.length() > BoardSummary.LENGTH_SUMMARY) {
                    contentSummary = contentSummary.substring(0, BoardSummary.LENGTH_SUMMARY);
                }
                converted.setContentSummary(contentSummary);
                return converted;
            });
        return boardSummaries;
    }
}
