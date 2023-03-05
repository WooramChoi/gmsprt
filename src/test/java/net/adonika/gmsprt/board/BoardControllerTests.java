package net.adonika.gmsprt.board;

import net.adonika.gmsprt.board.model.BoardResp;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.domain.BoardInfo;
import net.adonika.gmsprt.domain.UserInfo;
import net.adonika.gmsprt.user.UserManager;
import net.adonika.gmsprt.util.ObjectUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardControllerTests {

    private final Logger logger = LoggerFactory.getLogger(BoardControllerTests.class);

    @Autowired
    private BoardManager boardManager;

    @Autowired
    private UserManager userManager;


    @Test
    void createAndResponse() {

        UserInfo userInfo = new UserInfo();
        userInfo.setName("aaa");
        userInfo.setEmail("aaa@gmail.com");
        userInfo.setUrlPicture("https://");
        UserInfo savedUser01 = userManager.create(userInfo);
        Assertions.assertNotNull(savedUser01.getSeqUser());

        BoardInfo boardInfo = new BoardInfo();
        boardInfo.setTitle("test1");
        boardInfo.setContent("aaa01");
        BoardInfo savedBoard01 = boardManager.create(boardInfo, savedUser01.getSeqUser());
        Assertions.assertNotNull(savedBoard01.getSeqBoard());

        boardManager.findById(savedBoard01.getSeqBoard());

        BoardResp boardResp = boardManager.findById(savedBoard01.getSeqBoard(), BoardResp.class);
        Assertions.assertNotNull(boardResp.getUser());

        logger.info("boardResp: {}", ObjectUtil.toJson(boardResp));
    }
}
