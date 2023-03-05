package net.adonika.gmsprt.board.service.impl;

import net.adonika.gmsprt.board.dao.BoardDao;
import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardSpecificationBuilder;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.exception.FieldError;
import net.adonika.gmsprt.user.dao.UserDao;
import net.adonika.gmsprt.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

@Service("boardManager")
public class BoardManagerImpl implements BoardManager {

    private final Logger logger = LoggerFactory.getLogger(BoardManagerImpl.class);

    private final BoardDao boardDao;
    private final UserDao userDao;
    private final MessageSource messageSource;

    public BoardManagerImpl(BoardDao boardDao, UserDao userDao, MessageSource messageSource) {
        this.boardDao = boardDao;
        this.userDao = userDao;
        this.messageSource = messageSource;
    }

    @Override
    public BoardInfo create(BoardInfo boardInfo, Long seqUser) {

        if (boardInfo.getSeqBoard() != null && boardInfo.getSeqBoard() > 0) {
            throw ErrorResp.getConflict(
                    new FieldError(
                            "seqBoard", boardInfo.getSeqBoard(),
                            messageSource.getMessage("validation.is_null", new String[]{"boardInfo.seqBoard"}, Locale.getDefault())
                    )
            );
        }

        if (seqUser != null) {
            UserInfo userInfo = userDao.findById(seqUser).orElseThrow(
                    () -> ErrorResp.getNotFound(
                            new FieldError(
                                    "seqUser", seqUser,
                                    messageSource.getMessage("exception.not_found", null, Locale.getDefault())
                            )
                    )
            );
            boardInfo.setUserInfo(userInfo);
        } else {
            String pwd = boardInfo.getPwd();
            if (pwd != null) {
                boardInfo.setPwd(pwd);  //TODO 암호화
            }
        }

        return boardDao.save(boardInfo);
    }

    @Override
    public BoardInfo findById(Long seqBoard) {
        return boardDao.findById(seqBoard).orElseThrow(
                () -> ErrorResp.getNotFound(
                        new FieldError(
                                "seqBoard", seqBoard,
                                messageSource.getMessage("exception.not_found", null, Locale.getDefault())
                        )
                )
        );
    }

    @Override
    public List<BoardInfo> findAll(BoardForm boardForm) {
        return boardDao.findAll(new BoardSpecificationBuilder(boardForm).build());
    }

    /**
     *  BoardInfo 및 하위 Entity 를 VO 에 맵핑 *
     * @param boardInfo
     * @param c
     * @param <T>
     * @return
     */
    private <T> T convertBoard(BoardInfo boardInfo, Class<T> c) {

        // new instance of response object
        T instance;
        try {
            instance = c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw ErrorResp.getInternalServerError();
        }

        // copy properties
        BeanUtils.copyProperties(boardInfo, instance);
        logger.debug("set board instance: {}", ObjectUtil.toJson(instance));

        // set sub entities
        UserInfo userInfo = boardInfo.getUserInfo();
        if (userInfo != null) {
            logger.debug("has userInfo: {}", userInfo.getSeqUser());
            try {
                Field fieldUser = c.getDeclaredField("user");   // TODO "user" 라고 필드명을 정의해야 userInfo 에 연결되는 상황
                Object instanceUser = fieldUser.getType().newInstance();

                // copy properties
                BeanUtils.copyProperties(userInfo, instanceUser);
                logger.debug("set user instance: {}", ObjectUtil.toJson(instanceUser));

                fieldUser.setAccessible(true);
                fieldUser.set(instance, instanceUser);
                fieldUser.setAccessible(false);
                logger.debug("set user to board: {}", ObjectUtil.toJson(instance));

            } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    @Transactional
    @Override
    public <T> T findById(Long seqBoard, Class<T> c) {
        BoardInfo boardInfo = this.findById(seqBoard);
        return convertBoard(boardInfo, c);
    }

    @Transactional
    @Override
    public <T> Page<T> findAll(BoardForm boardForm, Class<T> c, Pageable pageable) {
        Page<BoardInfo> entities = boardDao.findAll(new BoardSpecificationBuilder(boardForm).build(), pageable);
        return entities.map(boardInfo -> convertBoard(boardInfo, c));
    }
}
