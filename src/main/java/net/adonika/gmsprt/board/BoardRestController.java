package net.adonika.gmsprt.board;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.model.BoardDetails;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardSearch;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.util.SecurityUtil;

@RestController
@RequestMapping(value = {"/boards"})
public class BoardRestController {
    
    private final Logger logger = LoggerFactory.getLogger(BoardRestController.class);

    private final BoardManager boardManager;

    public BoardRestController(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @PostMapping(value = {""})
    public ResponseEntity<BoardDetails> boardAdd(
            @RequestBody @Valid BoardAdd boardAdd
    ) {
        boardAdd.setSeqUser(SecurityUtil.getCurrentSeqUser());
        return ResponseEntity.ok(boardManager.addBoard(boardAdd));
    }

    @GetMapping(value = {"/{seqBoard}"})
    public ResponseEntity<BoardDetails> boardDetails(@PathVariable Long seqBoard) {
        return ResponseEntity.ok(boardManager.findBoard(seqBoard));
    }
    
    // NOTE Controller 는 무조건 Pageable 로 하자
    // NOTE 게시글 내용을 리스트에 담는건 비효율적인것 같다. 응답이 너무 늦어질거 같으면 BoardList VO 객체를 만들도록 하자
    @GetMapping(value = {"", "/"})
    public ResponseEntity<Page<BoardDetails>> boardList(BoardSearch boardSearch, @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(boardManager.findBoard(boardSearch, pageable));
    }
    
    @PatchMapping(value = {"/{seqBoard}"})
    public ResponseEntity<BoardDetails> boardModify(
            @PathVariable Long seqBoard,
            @RequestBody @Valid BoardModify boardModify
    ) {
        boardModify.setSeqUser(SecurityUtil.getCurrentSeqUser());
        return ResponseEntity.ok(boardManager.modifyBoard(seqBoard, boardModify));
    }
}
