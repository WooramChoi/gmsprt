package net.adonika.gmsprt.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import net.adonika.gmsprt.board.model.BoardAdd;
import net.adonika.gmsprt.board.model.BoardSearch;
import net.adonika.gmsprt.board.model.BoardModify;
import net.adonika.gmsprt.board.model.BoardDetails;

public interface BoardManager {
    
    BoardDetails addBoard(BoardAdd boardAdd);
    
    BoardDetails modifyBoard(Long seqBoard, BoardModify boardModify);
    
    void removeBoard(Long seqBoard);
    
    BoardDetails findBoard(Long seqBoard);
    
    List<BoardDetails> findBoard(BoardSearch boardSearch);
    
    Page<BoardDetails> findBoard(BoardSearch boardSearch, Pageable pageable);

    void removeBoard(BoardSearch boardSearch);
    
}
