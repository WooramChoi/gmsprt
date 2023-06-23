package net.adonika.gmsprt.board;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
import net.adonika.gmsprt.board.model.BoardSummary;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.file.model.FileDetails;
import net.adonika.gmsprt.file.service.FileManager;
import net.adonika.gmsprt.util.SecurityUtil;

@RestController
@RequestMapping(value = {"/boards"})
public class BoardRestController {
    
    private final Logger logger = LoggerFactory.getLogger(BoardRestController.class);

    private final BoardManager boardManager;
    private final FileManager fileManager;

    public BoardRestController(BoardManager boardManager, FileManager fileManager) {
        this.boardManager = boardManager;
        this.fileManager = fileManager;
    }

    @PostMapping(value = {""})
    public ResponseEntity<BoardDetails> boardAdd(
            @RequestBody @Valid BoardAdd boardAdd
    ) throws URISyntaxException {
        boardAdd.setSeqUser(SecurityUtil.getCurrentSeqUser());
        BoardDetails boardDetails = boardManager.addBoard(boardAdd);
        // NOTE 201 created 에선 대체적으로 body 가 없다고 한다.
        // 요청 body 를 기반으로 리소스가 생성됐기때문에 어차피 동일할 body 를 반환할 필요가 없다(단, 생성된 내용을 조회할 수 있는 URL은 반환해야한다).
        return ResponseEntity.created(URI.create("/boards/" + boardDetails.getSeqBoard())).body(boardDetails);
    }

    @GetMapping(value = {"/{seqBoard}"})
    public ResponseEntity<BoardDetails> boardDetails(@PathVariable Long seqBoard) {
        return ResponseEntity.ok(boardManager.findBoard(seqBoard));
    }
    
    // NOTE Controller 는 무조건 Pageable 로 하자
    @GetMapping(value = {"", "/"})
    public ResponseEntity<Page<BoardSummary>> boardList(BoardSearch boardSearch, @PageableDefault() Pageable pageable) {
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
    
    @GetMapping(value = {"/{seqBoard}/files"})
    public ResponseEntity<List<FileDetails>> boardFileList(@PathVariable Long seqBoard) {
        return ResponseEntity.ok(fileManager.findFile("BOARD_INFO", seqBoard));
    }
}
