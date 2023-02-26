package net.adonika.gmsprt.board;

import net.adonika.gmsprt.board.model.BoardResp;
import net.adonika.gmsprt.board.model.CreateBoardReq;
import net.adonika.gmsprt.board.service.BoardManager;
import net.adonika.gmsprt.security.model.OAuth2UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = {"/boards"})
public class BoardApiController {

    private final BoardManager boardManager;

    public BoardApiController(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @PostMapping(value = {""})
    public ResponseEntity<BoardResp> create(
            @RequestBody @Valid CreateBoardReq createBoardReq,
            Errors errors,
            @AuthenticationPrincipal OAuth2UserPrincipal oAuth2UserPrincipal
    ) {




        return null;
    }
}
