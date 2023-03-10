package net.adonika.gmsprt.board.dao;

import net.adonika.gmsprt.comm.dao.CommRepository;
import net.adonika.gmsprt.domain.BoardInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardDao extends CommRepository<BoardInfo, Long> {
}
