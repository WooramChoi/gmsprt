package net.adonika.gmsprt.board;

import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardResp;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.exception.ErrorResp;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.util.ObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class BoardManagerTests {

    private final Logger logger = LoggerFactory.getLogger(BoardManagerTests.class);

    @Autowired
    private BoardManager boardManager;

    @Autowired
    private UserManager userManager;

    private BoardInfo getBoardInfo(String title, String content, String name, String pwd) {
        BoardInfo boardInfo = new BoardInfo();
        boardInfo.setTitle(title);
        boardInfo.setContent(content);
        boardInfo.setName(name);
        boardInfo.setPwd(pwd);
        return boardInfo;
    }

    private UserInfo getUserInfo(String name, String email, String urlPicture) {
        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setEmail(email);
        userInfo.setUrlPicture(urlPicture);
        return userInfo;
    }

    @Test
    void insertDuplicate() {
        BoardInfo savedBoard01 = boardManager.create(getBoardInfo("tTitle", "tContent", "tName", "1234"), null);
        logger.info("Save Board: {}", savedBoard01.getSeqBoard());

        BoardInfo boardInfo = getBoardInfo("tTitle2", "tContent2", "tName2", "1234");
        boardInfo.setSeqBoard(savedBoard01.getSeqBoard());
        try {
            BoardInfo savedBoard02 = boardManager.create(boardInfo, null);
            logger.error("Should Not Save!");
        } catch (ErrorResp e) {
            logger.info("Done", e);
            Map<String, Object> data = e.toData();

            logger.info(ObjectUtil.toJson(data));
        }
    }

    @Test
    void insertAndSearch() {

        UserInfo savedUser = userManager.create(getUserInfo("ㄷㄷ", "dnfka4042@gmail.com", "https://lh3.googleusercontent.com/a-/AOh14Ggq5xpJ7amOLyLtL_CXkfftVcFrdKNv_o-MBqF32w"));

        BoardInfo savedBoard01 = boardManager.create(getBoardInfo("test1", "ㅇㅇ_1", "ㅇㅇ1", "1234"), null);
        Assertions.assertNotNull(savedBoard01.getSeqBoard());
        logger.info("Save Board: {}", savedBoard01.getSeqBoard());

        boardManager.create(getBoardInfo("test2", "ㅇㅇ_2", "ㅇㅇ2", "1234"), null);
        boardManager.create(getBoardInfo("test3", "ㅇㅇ_3", "ㅇㅇ3", "1234"), null);
        boardManager.create(getBoardInfo("test4", "ㅇㅇ_4", "ㅇㅇ4", "1234"), null);
        boardManager.create(getBoardInfo("test5", "ㅇㅇ_5", "ㅇㅇ5", "1234"), null);
        boardManager.create(getBoardInfo("test6", "ㄴㄴ_1", "ㄴㄴ1", "1234"), null);
        boardManager.create(getBoardInfo("test7", "ㄴㄴ_2", "ㄴㄴ2", "1234"), null);
        boardManager.create(getBoardInfo("test8", "ㄴㄴ_3", "ㄴㄴ3", "1234"), null);
        boardManager.create(getBoardInfo("test9", "ㄴㄴ_4", "ㄴㄴ4", "1234"), null);
        boardManager.create(getBoardInfo("test10", "ㄴㄴ_5", "ㄴㄴ5", "1234"), null);
        boardManager.create(getBoardInfo("test11", "ㄷㄷ_1", "ㄷㄷ1", "1234"), null);
        boardManager.create(getBoardInfo("test12", "ㄷㄷ_2", "ㄷㄷ2", "1234"), null);
        boardManager.create(getBoardInfo("test13", "ㄷㄷ_3", null, null), savedUser.getSeqUser());

        BoardForm boardForm = new BoardForm();
        Pageable pageable = PageRequest.of(0, 10);
        Page<BoardResp> searchedList = boardManager.findAll(boardForm, BoardResp.class, pageable);
        Assertions.assertEquals(10, searchedList.getSize());

        /*
            select * from board_info where name like '%ㄴㄴ%'
         */
        boardForm.setSelDetail("name");
        boardForm.setTxtDetail("ㄴㄴ");
        searchedList = boardManager.findAll(boardForm, BoardResp.class, pageable);
        Assertions.assertEquals(5, searchedList.getTotalElements());
        boardForm.setSelDetail(null);
        boardForm.setTxtDetail(null);

        /*
            select bi.* from board_info bi left outer join user_info ui on bi.seq_user = ui.seq_user
            where bi.name like '%ㄷㄷ%' or ui.name like '%ㄷㄷ%'
         */
        boardForm.setName("ㄷㄷ");
        searchedList = boardManager.findAll(boardForm, BoardResp.class, pageable);
        Assertions.assertEquals(3, searchedList.getTotalElements());
        boardForm.setName(null);

        /*
            select * from board_info
            where title like '%1%' or content like '%1%'
         */
        boardForm.setToc("1");
        searchedList = boardManager.findAll(boardForm, BoardResp.class, pageable);
        Assertions.assertEquals(6, searchedList.getTotalElements());

        /*
            select * from board_info
            where (title like '%1%' or content like '%1%') and name like '%ㄴㄴ%'
         */
        boardForm.setSelDetail("name");
        boardForm.setTxtDetail("ㄴㄴ");
        searchedList = boardManager.findAll(boardForm, BoardResp.class, pageable);
        Assertions.assertEquals(2, searchedList.getTotalElements());
    }

    @Test
    //@Transactional
    void fetchTest() {

        UserInfo savedUser01 = userManager.create(getUserInfo("aaa", "aaa@gmail.com", "https://"));
        UserInfo savedUser02 = userManager.create(getUserInfo("bbb", "bbb@gmail.com", "https://"));
        UserInfo savedUser03 = userManager.create(getUserInfo("ccc", "ccc@gmail.com", "https://"));

        boardManager.create(getBoardInfo("test1", "aaa01", null, null), savedUser01.getSeqUser());
        boardManager.create(getBoardInfo("test2", "aaa02", null, null), savedUser01.getSeqUser());
        boardManager.create(getBoardInfo("test3", "aaa03", null, null), savedUser01.getSeqUser());

        boardManager.create(getBoardInfo("test4", "bbb01", null, null), savedUser02.getSeqUser());
        boardManager.create(getBoardInfo("test5", "bbb02", null, null), savedUser02.getSeqUser());
        boardManager.create(getBoardInfo("test6", "bbb03", null, null), savedUser02.getSeqUser());

        boardManager.create(getBoardInfo("test7", "ccc01", null, null), savedUser03.getSeqUser());
        boardManager.create(getBoardInfo("test8", "ccc02", null, null), savedUser03.getSeqUser());
        boardManager.create(getBoardInfo("test9", "ccc03", null, null), savedUser03.getSeqUser());

        BoardForm boardForm = new BoardForm();
        List<BoardInfo> list = boardManager.findAll(boardForm);

        logger.info("Result set: {}", list.size());
//        for(int i=0; i<list.getSize(); i++){
//            if(i < 5){
//                BoardInfo boardInfo = list.getContent().get(i);
//                logger.info("[{}] {} _ {}", boardInfo.getSeqBoard(), boardInfo.getTitle(), boardInfo.getUserInfo().getName());
//            }else{
//                logger.info("GE 5");
//            }
//        }

    }
}
