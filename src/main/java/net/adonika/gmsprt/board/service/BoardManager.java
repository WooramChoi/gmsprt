package net.adonika.gmsprt.board.service;

import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.domain.BoardInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardManager {

    BoardInfo create(BoardInfo boardInfo, Long seqUser);

    BoardInfo findById(Long seqBoard);

    List<BoardInfo> findAll(BoardForm boardForm);

    <T> T findById(Long seqBoard, Class<T> c);

    <T> Page<T> findAll(BoardForm boardForm, Class<T> c, Pageable pageable);

}
