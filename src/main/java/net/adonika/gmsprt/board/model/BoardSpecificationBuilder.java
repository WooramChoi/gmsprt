package net.adonika.gmsprt.board.model;

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

        if(StringUtils.hasText(boardForm.getName())){
            spec = super.addSpecification(BoardInfo.class,
                    Operation.AND,
                    spec,
                    boardSpecification.likeName(boardForm.getName()));
        }

        if(StringUtils.hasText(boardForm.getToc())){
            spec = super.addSpecification(BoardInfo.class,
                    Operation.AND,
                    spec,
                    boardSpecification.likeTitleOrContent(boardForm.getToc()));
        }

        return spec;
    }

}
