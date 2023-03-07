package net.adonika.gmsprt.board;

import net.adonika.gmsprt.board.model.BoardVO;
import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.security.model.OAuth2UserPrincipal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/boards"})
public class BoardApiController {

    private final BoardManager boardManager;

    public BoardApiController(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @PostMapping(value = {""})
    public ResponseEntity<BoardVO> create(
            @RequestBody @Valid BoardAdd createBoardReq,
            Errors errors,
            @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal
    ) {


        return null;
    }

    /**
     * 임시 컨트롤러
     *
     * @param seqBoard
     * @return
     */
    @GetMapping(value = {"/{seqBoard}"})
    public ResponseEntity<BoardVO> getOne(@PathVariable Long seqBoard) {
        BoardVO boardVO = boardManager.findBoard(seqBoard);

//        BoardResp boardResp = new BoardResp();
//        boardResp.setSeqBoard(boardInfo.getSeqBoard());
//        boardResp.setTitle(boardInfo.getTitle());
//        boardResp.setContent(boardInfo.getContent());
//
//        UserInfo userInfo = boardInfo.getUserInfo();
//        if(userInfo != null){
//            boardResp.setName(userInfo.getName());
//            boardResp.setEmail(userInfo.getEmail());
//            boardResp.setUrlPicture(userInfo.getUrlPicture());
//        }else{
//            boardResp.setName(boardInfo.getName());
//        }

        return ResponseEntity.ok(boardVO);
    }
}
