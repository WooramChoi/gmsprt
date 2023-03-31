package net.adonika.gmsprt.board;

import net.adonika.gmsprt.board.model.BoardSearch;
import net.adonika.gmsprt.comm.CommSpecificationBuilder;
import net.adonika.gmsprt.domain.BoardInfo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BoardSpecificationBuilder extends CommSpecificationBuilder {

    private final BoardSearch boardSearch;

    public BoardSpecificationBuilder(BoardSearch boardSearch) {
        this.boardSearch = boardSearch;
    }

    public Specification<BoardInfo> build() {
        Specification<BoardInfo> spec = super.build(BoardInfo.class, boardSearch);
        BoardSpecification boardSpecification = new BoardSpecification();

        if (StringUtils.hasText(boardSearch.getName())) {
            spec = super.addSpecification(Operation.AND,
                    spec,
                    boardSpecification.likeName(boardSearch.getName()));
        }

        if (StringUtils.hasText(boardSearch.getToc())) {
            spec = super.addSpecification(Operation.AND,
                    spec,
                    boardSpecification.likeTitleOrContent(boardSearch.getToc()));
        }
        
        if (boardSearch.getUse() != null) {
            spec = super.addSpecification(Operation.AND,
                    spec,
                    boardSpecification.isUse(boardSearch.getUse()));
        }

        return spec;
    }

}
