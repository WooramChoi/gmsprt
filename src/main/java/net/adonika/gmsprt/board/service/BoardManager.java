package net.adonika.gmsprt.board.service;

import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.domain.BoardInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardManager {

    Page<BoardInfo> list(BoardForm boardForm, Pageable pageable);

    BoardInfo create(BoardInfo boardInfo, Long seqUser);

    BoardInfo getOne(Long seqBoard);

}
