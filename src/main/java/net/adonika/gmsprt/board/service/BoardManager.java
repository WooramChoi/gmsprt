package net.adonika.gmsprt.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardVO;

public interface BoardManager {
    
    BoardVO addBoard(BoardAdd boardAdd, Long seqUser);
    
    BoardVO modifyBoard(Long seqBoard, BoardModify boardModify, Long seqUser);
    
    void removeBoard(Long seqBoard);
    
    BoardVO findBoard(Long seqBoard);
    
    List<BoardVO> findBoard(BoardForm boardForm);
    
    Page<BoardVO> findBoard(BoardForm boardForm, Pageable pageable);

    void removeBoard(BoardForm boardForm);
    
}
