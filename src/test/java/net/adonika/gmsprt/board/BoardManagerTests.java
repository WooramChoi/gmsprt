package net.adonika.gmsprt.board;

import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.user.UserManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class BoardManagerTests {

    private final Logger logger = LoggerFactory.getLogger(BoardManagerTests.class);

    @Autowired
    private BoardManager boardManager;

    @Autowired
    private UserManager userManager;

    //@Test
    void insertAndSearch() {

        UserInfo savedUser = userManager.create("ㄷㄷ", "dnfka4042@gmail.com", "https://lh3.googleusercontent.com/a-/AOh14Ggq5xpJ7amOLyLtL_CXkfftVcFrdKNv_o-MBqF32w");

        BoardInfo savedBoard01 = boardManager.create("test1", "ㅇㅇ_1", "ㅇㅇ1", "1234");
        Assertions.assertNotNull(savedBoard01.getSeqBoard());
        logger.info("Save Board: {}", savedBoard01.getSeqBoard());

        boardManager.create("test2", "ㅇㅇ_2", "ㅇㅇ2", "1234");
        boardManager.create("test3", "ㅇㅇ_3", "ㅇㅇ3", "1234");
        boardManager.create("test4", "ㅇㅇ_4", "ㅇㅇ4", "1234");
        boardManager.create("test5", "ㅇㅇ_5", "ㅇㅇ5", "1234");
        boardManager.create("test6", "ㄴㄴ_1", "ㄴㄴ1", "1234");
        boardManager.create("test7", "ㄴㄴ_2", "ㄴㄴ2", "1234");
        boardManager.create("test8", "ㄴㄴ_3", "ㄴㄴ3", "1234");
        boardManager.create("test9", "ㄴㄴ_4", "ㄴㄴ4", "1234");
        boardManager.create("test10", "ㄴㄴ_5", "ㄴㄴ5", "1234");
        boardManager.create("test11", "ㄷㄷ_1", "ㄷㄷ1", "1234");
        boardManager.create("test12", "ㄷㄷ_2", "ㄷㄷ2", "1234");
        boardManager.create("test13", "ㄷㄷ_3", savedUser.getSeqUser());

        BoardForm boardForm = new BoardForm();
        Pageable pageable = PageRequest.of(0, 10);
        Page<BoardInfo> searchedList = boardManager.list(boardForm, pageable);
        Assertions.assertEquals(10, searchedList.getSize());

        /*
            select * from board_info where name like '%ㄴㄴ%'
         */
        boardForm.setSelDetail("name");
        boardForm.setTxtDetail("ㄴㄴ");
        searchedList = boardManager.list(boardForm, pageable);
        Assertions.assertEquals(5, searchedList.getTotalElements());
        boardForm.setSelDetail(null);
        boardForm.setTxtDetail(null);

        /*
            select bi.* from board_info bi left outer join user_info ui on bi.seq_user = ui.seq_user
            where bi.name like '%ㄷㄷ%' or ui.name like '%ㄷㄷ%'
         */
        boardForm.setName("ㄷㄷ");
        searchedList = boardManager.list(boardForm, pageable);
        Assertions.assertEquals(3, searchedList.getTotalElements());
        boardForm.setName(null);

        /*
            select * from board_info
            where title like '%1%' or content like '%1%'
         */
        boardForm.setToc("1");
        searchedList = boardManager.list(boardForm, pageable);
        Assertions.assertEquals(6, searchedList.getTotalElements());

        /*
            select * from board_info
            where (title like '%1%' or content like '%1%') and name like '%ㄴㄴ%'
         */
        boardForm.setSelDetail("name");
        boardForm.setTxtDetail("ㄴㄴ");
        searchedList = boardManager.list(boardForm, pageable);
        Assertions.assertEquals(2, searchedList.getTotalElements());
    }

    @Test
    //@Transactional
    void fetchTest() {

        UserInfo savedUser01 = userManager.create("aaa", "aaa@gmail.com", "https://");

        UserInfo savedUser02 = userManager.create("bbb", "bbb@gmail.com", "https://");

        UserInfo savedUser03 = userManager.create("ccc", "ccc@gmail.com", "https://");

        boardManager.create("test1", "aaa01", savedUser01.getSeqUser());
        boardManager.create("test2", "aaa02", savedUser01.getSeqUser());
        boardManager.create("test3", "aaa03", savedUser01.getSeqUser());

        boardManager.create("test4", "bbb01", savedUser02.getSeqUser());
        boardManager.create("test5", "bbb02", savedUser02.getSeqUser());
        boardManager.create("test6", "bbb03", savedUser02.getSeqUser());

        boardManager.create("test7", "ccc01", savedUser03.getSeqUser());
        boardManager.create("test8", "ccc02", savedUser03.getSeqUser());
        boardManager.create("test9", "ccc03", savedUser03.getSeqUser());

        BoardForm boardForm = new BoardForm();
        Pageable pageable = PageRequest.of(0, 10);
        Page<BoardInfo> list = boardManager.list(boardForm, pageable);

        logger.info("Result set: {}", list.getSize());
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
