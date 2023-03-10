package net.adonika.gmsprt.board;

import javax.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import net.adonika.gmsprt.comm.CommSpecification;
import net.adonika.gmsprt.domain.BoardInfo;

public class BoardSpecification extends CommSpecification {
    
    private final Class<BoardInfo> c = BoardInfo.class;

    Specification<BoardInfo> likeName(String name) {

        Specification<BoardInfo> specBILikeName = super.like(c, "name", name);
        Specification<BoardInfo> specUILikeName = (root, query, cb) -> cb.like(root.join("userInfo", JoinType.LEFT).get("name"), name);

        return specBILikeName.or(specUILikeName);
    }

    Specification<BoardInfo> likeTitleOrContent(String toc) {

        Specification<BoardInfo> specLikeTitle = super.like(c, "title", toc);
        Specification<BoardInfo> specLikeContent = super.like(c, "content", toc);

        return specLikeTitle.or(specLikeContent);
    }
    
    Specification<BoardInfo> isUse(boolean use) {
        
        Specification<BoardInfo> specIsUse;
        if (use) {
            specIsUse = super.isTrue(c, "use");
        } else {
            specIsUse = super.isFalse(c, "use");
        }
        
        return specIsUse;
    }
}
