package net.adonika.gmsprt.board;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardVO;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.security.model.OAuth2UserPrincipal;

@RestController
@RequestMapping(value = {"/boards"})
public class BoardRestController {
    
    private final Logger logger = LoggerFactory.getLogger(BoardRestController.class);

    private final BoardManager boardManager;

    public BoardRestController(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @PostMapping(value = {""})
    public ResponseEntity<BoardVO> boardAdd(
            @RequestBody @Valid BoardAdd boardAdd,
            @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal
    ) {
        
        Long seqUser;
        if (oAuth2UserPrincipal != null) {
            seqUser = oAuth2UserPrincipal.getSeqUser();
        } else {
            seqUser = null;
        }
        boardAdd.setSeqUser(seqUser);

        return ResponseEntity.ok(boardManager.addBoard(boardAdd));
    }

    @GetMapping(value = {"/{seqBoard}"})
    public ResponseEntity<BoardVO> boardDetails(@PathVariable Long seqBoard) {
        return ResponseEntity.ok(boardManager.findBoard(seqBoard));
    }
    
    // Controller ??? ????????? Pageable ??? ??????
    @GetMapping(value = {"", "/"})
    public ResponseEntity<Page<BoardVO>> boardList(BoardForm boardForm, @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(boardManager.findBoard(boardForm, pageable));
    }
    
    @PatchMapping(value = {"/{seqBoard}"})
    public ResponseEntity<BoardVO> boardModify(
            @PathVariable Long seqBoard,
            @RequestBody @Valid BoardModify boardModify,
            @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal
    ) {
        
        Long seqUser;
        if (oAuth2UserPrincipal != null) {
            seqUser = oAuth2UserPrincipal.getSeqUser();
        } else {
            seqUser = null;
        }
        boardModify.setSeqUser(seqUser);
        
        return ResponseEntity.ok(boardManager.modifyBoard(seqBoard, boardModify));
    }
}
