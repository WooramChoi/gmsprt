package net.adonika.gmsprt.board;

import net.adonika.gmsprt.board.model.BoardForm;
import net.adonika.gmsprt.comm.CommSpecificationBuilder;
import net.adonika.gmsprt.domain.BoardInfo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BoardSpecificationBuilder extends CommSpecificationBuilder {

    private final BoardForm boardForm;

    public BoardSpecificationBuilder(BoardForm boardForm) {
        this.boardForm = boardForm;
    }

    public Specification<BoardInfo> build() {
        Specification<BoardInfo> spec = super.build(BoardInfo.class, boardForm);
        BoardSpecification boardSpecification = new BoardSpecification();

        if (StringUtils.hasText(boardForm.getName())) {
            spec = super.addSpecification(Operation.AND,
                    spec,
                    boardSpecification.likeName(boardForm.getName()));
        }

        if (StringUtils.hasText(boardForm.getToc())) {
            spec = super.addSpecification(Operation.AND,
                    spec,
                    boardSpecification.likeTitleOrContent(boardForm.getToc()));
        }
        
        if (boardForm.getUse() != null) {
            spec = super.addSpecification(Operation.AND,
                    spec,
                    boardSpecification.isUse(boardForm.getUse()));
        }

        return spec;
    }

}
