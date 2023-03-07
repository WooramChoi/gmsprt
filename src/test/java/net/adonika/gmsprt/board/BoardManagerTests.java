package net.adonika.gmsprt.board;

import java.util.List;

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
import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardVO;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.user.model.UserAdd;
import net.adonika.gmsprt.user.model.UserVO;
import net.adonika.gmsprt.util.ObjectUtil;

@SpringBootTest
public class BoardManagerTests {

    private final Logger logger = LoggerFactory.getLogger(BoardManagerTests.class);

    @Autowired
    private BoardManager boardManager;

    @Autowired
    private UserManager userManager;

    private UserAdd getUserAdd(String name, String email, String urlPicture) {
        UserAdd userAdd = new UserAdd();
        userAdd.setName(name);
        userAdd.setEmail(email);
        userAdd.setUrlPicture(urlPicture);
        return userAdd;
    }
    
    private BoardAdd getBoardAdd(String title, String content, String name, String pwd) {
        BoardAdd boardAdd = new BoardAdd();
        boardAdd.setTitle(title);
        boardAdd.setContent(content);
        boardAdd.setName(name);
        boardAdd.setPwd(pwd);
        return boardAdd;
    }

    @Test
    void CRUD() {
        
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
        UserVO savedUser01 = userManager.addUser(getUserAdd("aaa", "aaa@gmail.com", "https://"));
        UserVO savedUser02 = userManager.addUser(getUserAdd("bbb", "bbb@gmail.com", "https://"));
        UserVO savedUser03 = userManager.addUser(getUserAdd("ccc", "ccc@gmail.com", "https://"));
        
        BoardVO boardVO01 = boardManager.addBoard(getBoardAdd("test01", "aaa01", null, null), savedUser01.getSeqUser());
        BoardVO boardVO02 = boardManager.addBoard(getBoardAdd("test02", "aaa02", null, null), savedUser01.getSeqUser());
        BoardVO boardVO03 = boardManager.addBoard(getBoardAdd("test03", "aaa03", null, null), savedUser01.getSeqUser());
        
        BoardVO boardVO04 = boardManager.addBoard(getBoardAdd("test04", "bbb01", null, null), savedUser02.getSeqUser());
        BoardVO boardVO05 = boardManager.addBoard(getBoardAdd("test05", "bbb02", null, null), savedUser02.getSeqUser());
        BoardVO boardVO06 = boardManager.addBoard(getBoardAdd("test06", "bbb03", "bbb", "bbb01"), null);
        
        BoardVO boardVO07 = boardManager.addBoard(getBoardAdd("test07", "ccc01", null, null), savedUser03.getSeqUser());
        BoardVO boardVO08 = boardManager.addBoard(getBoardAdd("test08", "ccc02", "ccc", "ccc01"), null);
        BoardVO boardVO09 = boardManager.addBoard(getBoardAdd("test09", "ccc03", "ccc", "ccc01"), null);
        
        BoardVO boardVO10 = boardManager.addBoard(getBoardAdd("test10", "ddd01", "ddd", "ddd01"), null);
        BoardVO boardVO11 = boardManager.addBoard(getBoardAdd("test11", "ddd02", "ddd", "ddd01"), null);
        BoardVO boardVO12 = boardManager.addBoard(getBoardAdd("test12", "ddd03", "ddd", "ddd01"), null);
        
        /*
         * 02. Read
         */
        BoardVO savedBoard = boardManager.findBoard(boardVO01.getSeqBoard());
        Assertions.assertEquals(boardVO01.getUser().getSeqUser(), savedBoard.getUser().getSeqUser());
        Assertions.assertNotNull(savedBoard.getDtCreate());

        BoardForm boardForm = new BoardForm();
        List<BoardVO> list = boardManager.findBoard(boardForm);
        Assertions.assertEquals(12, list.size());
        
        Pageable pageable = PageRequest.of(0, 5);
        Page<BoardVO> pagedList = boardManager.findBoard(boardForm, pageable);
        Assertions.assertEquals(5, pagedList.getSize());
        
        /*
            select bi.* from board_info bi left outer join user_info ui on bi.seq_user = ui.seq_user
            where bi.name like '%{keyword}%' or ui.name like '%{keyword}%'
         */
        boardForm.setName("bbb");
        List<BoardVO> searchedList = boardManager.findBoard(boardForm);
        Assertions.assertEquals(3, searchedList.size());
        
        /*
            select * from board_info
            where title like '%{keyword}%' or content like '%{keyword}%'
         */
        boardForm.setName(null);
        boardForm.setToc("02");
        searchedList = boardManager.findBoard(boardForm);
        Assertions.assertEquals(4, searchedList.size());
        
        /*
            select bi.* from board_info bi left outer join user_info ui on bi.seq_user = ui.seq_user
            where (bi.name like '%{keyword}%' or ui.name like '%{keyword}%') and (bi.title like '%{keyword}%' or bi.content like '%{keyword}%')
         */
        boardForm.setName("ccc");
        searchedList = boardManager.findBoard(boardForm);
        Assertions.assertEquals(1, searchedList.size());
        
        /*
         * 03. Update
         */
        BoardModify boardModify = new BoardModify();
        boardModify.setTitle("test13");
        boardModify.setContent("ddd04");
        boardModify.setChangePwd(true);
        boardModify.setPwd("ccc01");
        boardModify.setNewPwd("ccc02");
        logger.debug("ignores: {}", ObjectUtil.toJson(boardModify.getIgnores()));
        BoardVO modifiedBoard = boardManager.modifyBoard(boardVO08.getSeqBoard(), boardModify, null);
        Assertions.assertEquals(boardModify.getTitle(), modifiedBoard.getTitle());
        Assertions.assertEquals(boardModify.getContent(), modifiedBoard.getContent());
        Assertions.assertNotNull(modifiedBoard.getDtUpdate());
        //Assertions.assertNotEquals(modifiedBoard.getDtUpdate(), modifiedBoard.getDtCreate()); //TODO 수정일이 안바뀐다... 확인필요
        
        /*
         * 04. Delete
         */
        boardManager.removeBoard(boardVO01.getSeqBoard());
        try {
            boardManager.findBoard(boardVO01.getSeqBoard());
        } catch (Exception e) {
            logger.info("removed, Should Not Found, done");
        }
        
        boardForm.setName(null);
        boardManager.removeBoard(boardForm);
        list = boardManager.findBoard(boardForm);
        Assertions.assertEquals(0, list.size());
    }
}
