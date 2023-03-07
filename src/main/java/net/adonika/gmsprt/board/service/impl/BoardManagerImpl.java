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

import net.adonika.gmsprt.board.dao.BoardDao;
import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardSpecificationBuilder;
import net.adonika.gmsprt.board.model.BoardVO;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.exception.ErrorResp;
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

        /*
         * TODO Entity 객체 내에 "userInfo" 라고 필드명을 정의해야하는 상황
         */
        BeanUtils.copyProperties(boardInfo, instance, "userInfo");
        logger.debug("[convertTo] copy boardInfo to {} done: {}", c.getSimpleName(), ObjectUtil.toJson(instance));

        UserInfo userInfo = boardInfo.getUserInfo();
        if (userInfo != null) {
            logger.debug("[convertTo] boardInfo has UserInfo: seqUser = {}", userInfo.getSeqUser());
            
            try {
                /*
                 * TODO VO 객체 내에 "user" 라고 필드명을 정의해야 userInfo 에 연결되는 상황
                 */
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
    public BoardVO addBoard(BoardAdd boardAdd, Long seqUser) {
        logger.info("[addBoard] start");
        BoardInfo boardInfo = new BoardInfo();
        BeanUtils.copyProperties(boardAdd, boardInfo);
        
        if (Optional.ofNullable(seqUser).orElse(0L) > 0L) {
            logger.info("[addBoard] has UserInfo: seqUser = {}", seqUser);
            UserInfo userInfo = userDao.getById(seqUser);
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
        return convertTo(savedBoardInfo, BoardVO.class);
    }

    @Transactional
    @Override
    public BoardVO modifyBoard(Long seqBoard, BoardModify boardModify, Long seqUser) {
        logger.info("[modifyBoard] start: seqBoard = {} / seqUser = {}", seqBoard, seqUser);
        BoardInfo boardInfo = boardDao.getById(seqBoard);
        
        // TODO 비즈니스 로직이 Service 에 존재해도 괜찮은가?
        // 일단 비밀번호가 VO 객체에 존재하지 않길 바라서 이런 모양이 됨.
        logger.info("[modifyBoard] check authorization");
        if (boardInfo.getUserInfo() != null) {
            logger.info("[modifyBoard] check by owner");
            logger.debug("[modifyBoard] owner = {} / accessor = {}", boardInfo.getUserInfo().getSeqUser(), seqUser);
            if (boardInfo.getUserInfo().getSeqUser() != seqUser) {
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
                    logger.debug("[modifyBoard] owner pwd = {} / accessor pwd = {}", boardInfo.getPwd(), "Unknown");
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
        
        // 글의 작성자를 변경하는 상황은 없을것이라고 간주. 급할경우 DB를 변경해야할것
//        if (Optional.ofNullable(seqUser).orElse(0L) > 0L) {
//            logger.info("[modifyBoard] has UserInfo: seqUser = {}", seqUser);
//            UserInfo userInfo = userDao.getById(seqUser);
//            boardInfo.setUserInfo(userInfo);
//            logger.info("[modifyBoard] set UserInfo done");
//        }
        
        if (boardModify.isChangePwd()) {
            logger.info("[modifyBoard] change pwd");
            if (!StringUtils.hasText(boardModify.getNewPwd())) {
                boardInfo.setPwd(null);
            } else {
                String pwd = passwordEncoder.encode(boardModify.getNewPwd());
                boardInfo.setPwd(pwd);
            }
            logger.info("[modifyBoard] set new pwd done");
        }
        
        BoardInfo savedBoardInfo = boardDao.save(boardInfo);
        logger.info("[modifyBoard] done: seqBoard = {}", savedBoardInfo.getSeqBoard());
        return convertTo(savedBoardInfo, BoardVO.class);
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
    public void removeBoard(BoardForm boardForm) {
        logger.info("[removeBoard] start: boardForm = {}", ObjectUtil.toJson(boardForm));
        List<BoardInfo> list = boardDao.findAll(new BoardSpecificationBuilder(boardForm).build());
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
    public BoardVO findBoard(Long seqBoard) {
        logger.info("[findBoard] start: seqBoard = {}", seqBoard);
        BoardInfo savedBoardInfo = boardDao.getById(seqBoard);
        logger.info("[findBoard] done: seqBoard = {}", seqBoard);
        return convertTo(savedBoardInfo, BoardVO.class);
    }

    @Transactional
    @Override
    public List<BoardVO> findBoard(BoardForm boardForm) {
        logger.info("[findBoard] start: boardForm = {}", ObjectUtil.toJson(boardForm));
        List<BoardInfo> savedBoardInfos = boardDao.findAll(new BoardSpecificationBuilder(boardForm).build());
        logger.info("[findBoard] done: savedBoardInfos[{}]", savedBoardInfos.size());
        List<BoardVO> list = new ArrayList<>();
        savedBoardInfos.forEach(boardInfo -> list.add(convertTo(boardInfo, BoardVO.class)));
        return list;
    }

    @Transactional
    @Override
    public Page<BoardVO> findBoard(BoardForm boardForm, Pageable pageable) {
        logger.info("[findBoard] start: boardForm = {} / pageable = {}", ObjectUtil.toJson(boardForm), ObjectUtil.toJson(pageable));
        Page<BoardInfo> savedBoardInfos = boardDao.findAll(new BoardSpecificationBuilder(boardForm).build(), pageable);
        logger.info("[findBoard] done: savedBoardInfos[{}]", savedBoardInfos.getSize());
        return savedBoardInfos.map(boardInfo -> convertTo(boardInfo, BoardVO.class));
    }
}
