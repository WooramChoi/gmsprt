package net.adonika.gmsprt.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.user.service.UserManager;

@SpringBootTest
public class BoardControllerTests {

    private final Logger logger = LoggerFactory.getLogger(BoardControllerTests.class);

    @Autowired
    private BoardManager boardManager;

    @Autowired
    private UserManager userManager;

}
