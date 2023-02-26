package net.adonika.gmsprt.board.model;

import net.adonika.gmsprt.comm.CommSpecification;
import net.adonika.gmsprt.domain.BoardInfo;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class BoardSpecification extends CommSpecification {

    Specification<BoardInfo> likeName(String name) {

        Specification<BoardInfo> specBILikeName = super.like(BoardInfo.class, "name", name);
        Specification<BoardInfo> specUILikeName = (root, query, cb) -> cb.like(root.join("userInfo", JoinType.LEFT).get("name"), name);

        return specBILikeName.or(specUILikeName);
    }

    Specification<BoardInfo> likeTitleOrContent(String toc) {

        Specification<BoardInfo> specLikeTitle = super.like(BoardInfo.class, "title", toc);
        Specification<BoardInfo> specLikeContent = super.like(BoardInfo.class, "content", toc);

        return specLikeTitle.or(specLikeContent);
    }
}
