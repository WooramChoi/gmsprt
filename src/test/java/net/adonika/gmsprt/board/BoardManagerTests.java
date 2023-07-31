package net.adonika.gmsprt.board;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.model.BoardDetails;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardSearch;
import net.adonika.gmsprt.board.model.BoardSummary;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserDetails;
import net.adonika.gmsprt.user.service.UserManager;
import net.adonika.gmsprt.util.ObjectUtil;

@SpringBootTest(properties = {"spring.config.location=classpath:application.properties"})
public class BoardManagerTests {

    private final Logger logger = LoggerFactory.getLogger(BoardManagerTests.class);

    @Autowired
    private BoardManager boardManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    Validator validator;

    private UserAdd getUserAdd(String name, String email, String urlPicture) {
        UserAdd userAdd = new UserAdd();
        userAdd.setName(name);
        userAdd.setEmail(email);
        userAdd.setUrlPicture(urlPicture);
        return userAdd;
    }
    
    private BoardAdd getBoardAdd(String title, String content, Long seqUser, String name, String pwd) {
        BoardAdd boardAdd = new BoardAdd();
        boardAdd.setTitle(title);
        boardAdd.setContent(content);
        boardAdd.setPlainText(content);
        boardAdd.setSeqUser(seqUser);
        boardAdd.setName(name);
        boardAdd.setPwd(pwd);
        return boardAdd;
    }
    
    // TODO 기존 데이터를 건드리지 않는 테스트가 필요하다
    //@Test
    void CRUD() {
        
        /*
         * 00. Remove data-h2.sql 
         */
        boardManager.removeBoard(new BoardSearch());
        
        /*
         * 00. Null Check
         */
        try {
            boardManager.findBoard(1L);
        } catch (Exception e) {
            logger.info("Should Not Found, done");
        }

        /*
         * 01. Create
         */
        UserDetails savedUser01 = userManager.addUser(getUserAdd("aaa", "aaa@gmail.com", "https://"));
        UserDetails savedUser02 = userManager.addUser(getUserAdd("bbb", "bbb@gmail.com", "https://"));
        UserDetails savedUser03 = userManager.addUser(getUserAdd("ccc", "ccc@gmail.com", "https://"));
        
        BoardDetails boardVO01 = boardManager.addBoard(getBoardAdd("test01", "aaa01", savedUser01.getSeqUser(), null, null));
        BoardDetails boardVO02 = boardManager.addBoard(getBoardAdd("test02", "aaa02", savedUser01.getSeqUser(), null, null));
        BoardDetails boardVO03 = boardManager.addBoard(getBoardAdd("test03", "aaa03", savedUser01.getSeqUser(), null, null));
        
        BoardDetails boardVO04 = boardManager.addBoard(getBoardAdd("test04", "bbb01", savedUser02.getSeqUser(), null, null));
        BoardDetails boardVO05 = boardManager.addBoard(getBoardAdd("test05", "bbb02", savedUser02.getSeqUser(), null, null));
        BoardDetails boardVO06 = boardManager.addBoard(getBoardAdd("test06", "bbb03", null, "bbb", "bbb01"));
        
        BoardDetails boardVO07 = boardManager.addBoard(getBoardAdd("test07", "ccc01", savedUser03.getSeqUser(), null, null));
        BoardDetails boardVO08 = boardManager.addBoard(getBoardAdd("test08", "ccc02", null, "ccc", "ccc01"));
        BoardDetails boardVO09 = boardManager.addBoard(getBoardAdd("test09", "ccc03", null, "ccc", "ccc01"));
        
        BoardDetails boardVO10 = boardManager.addBoard(getBoardAdd("test10", "ddd01", null, "ddd", "ddd01"));
        BoardDetails boardVO11 = boardManager.addBoard(getBoardAdd("test11", "ddd02", null, "ddd", "ddd01"));
        BoardDetails boardVO12 = boardManager.addBoard(getBoardAdd("test12", "ddd03", null, "ddd", "ddd01"));
        
        /*
         * 02. Read
         */
        BoardDetails savedBoard = boardManager.findBoard(boardVO01.getSeqBoard());
        Assertions.assertEquals(boardVO01.getUser().getSeqUser(), savedBoard.getUser().getSeqUser());
        Assertions.assertNotNull(savedBoard.getDtCreate());

        BoardSearch boardSearch = new BoardSearch();
        List<BoardDetails> list = boardManager.findBoard(boardSearch);
        Assertions.assertEquals(12, list.size());
        
        Pageable pageable = PageRequest.of(0, 5);
        Page<BoardSummary> pagedList = boardManager.findBoard(boardSearch, pageable);
        Assertions.assertEquals(5, pagedList.getSize());
        
        /*
            select bi.* from board_info bi left outer join user_info ui on bi.seq_user = ui.seq_user
            where bi.name like '%{keyword}%' or ui.name like '%{keyword}%'
         */
        boardSearch.setName("bbb");
        List<BoardDetails> searchedList = boardManager.findBoard(boardSearch);
        Assertions.assertEquals(3, searchedList.size());
        
        /*
            select * from board_info
            where title like '%{keyword}%' or content like '%{keyword}%'
         */
        boardSearch.setName(null);
        boardSearch.setToc("02");
        searchedList = boardManager.findBoard(boardSearch);
        Assertions.assertEquals(4, searchedList.size());
        
        /*
            select bi.* from board_info bi left outer join user_info ui on bi.seq_user = ui.seq_user
            where (bi.name like '%{keyword}%' or ui.name like '%{keyword}%') and (bi.title like '%{keyword}%' or bi.content like '%{keyword}%')
         */
        boardSearch.setName("ccc");
        searchedList = boardManager.findBoard(boardSearch);
        Assertions.assertEquals(1, searchedList.size());
        
        /*
         * 03. Update
         */
        BoardModify boardModify = new BoardModify();
        boardModify.setTitle("test13");

        boardModify.setContent("");
        Set<ConstraintViolation<BoardModify>> validate = validator.validate(boardModify);
        Assertions.assertFalse(validate.isEmpty());
        for (ConstraintViolation<BoardModify> constraintViolation : validate) {
            logger.error("field: {}", constraintViolation.getPropertyPath().toString());
            logger.error("value: {}", constraintViolation.getInvalidValue());
            logger.error("reason: {}", constraintViolation.getMessage());
        }
        boardModify.setContent("ddd04");

        boardModify.setPwd("ccc01");
        boardModify.setNewPwd("ccc02");
        logger.debug("ignores: {}", ObjectUtil.toJson(boardModify.getIgnores()));
        BoardDetails modifiedBoard = boardManager.modifyBoard(boardVO08.getSeqBoard(), boardModify);
        Assertions.assertEquals(boardModify.getTitle(), modifiedBoard.getTitle());
        Assertions.assertEquals(boardModify.getContent(), modifiedBoard.getContent());
        Assertions.assertNotNull(modifiedBoard.getDtUpdate());
        Assertions.assertNotEquals(modifiedBoard.getDtUpdate(), modifiedBoard.getDtCreate());
        
        /*
         * 04. Delete
         */
        boardManager.removeBoard(boardVO01.getSeqBoard());
        try {
            boardManager.findBoard(boardVO01.getSeqBoard());
        } catch (Exception e) {
            logger.info("removed, Should Not Found, done");
        }
        
        /*
         * 05. Delete by Query
         */
        boardSearch.setName(null);
        boardManager.removeBoard(boardSearch);
        list = boardManager.findBoard(boardSearch);
        Assertions.assertEquals(0, list.size());
    }
}
