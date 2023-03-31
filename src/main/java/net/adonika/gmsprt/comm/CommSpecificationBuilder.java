package net.adonika.gmsprt.comm;

import java.text.ParseException;
import java.util.Date;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import net.adonika.gmsprt.comm.model.CommSearch;
import net.adonika.gmsprt.util.DateUtil;

public class CommSpecificationBuilder {

    public enum Operation {
        AND, OR
    }

    public <T> Specification<T> build(Class<T> c, CommSearch commSearch) {

        Specification<T> spec = null;
        CommSpecification commSpecification = new CommSpecification();

        // 날짜 검색
        if (StringUtils.hasText(commSearch.getSelDtKind())) {
            // 날짜 - From
            if (StringUtils.hasText(commSearch.getTxtDtFrom())) {
                try {
                    Date dtFrom = DateUtil.stringToDate(commSearch.getTxtDtFrom() + "000000", "yyyyMMddHHmmss");
                    spec = addSpecification(Operation.AND,
                            spec,
                            commSpecification.from(c, commSearch.getSelDtKind(), dtFrom));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // 날짜 - To
            if (StringUtils.hasText(commSearch.getTxtDtTo())) {
                try {
                    Date dtTo = DateUtil.stringToDate(commSearch.getTxtDtTo() + "000000", "yyyyMMddHHmmss");
                    spec = addSpecification(Operation.AND,
                            spec,
                            commSpecification.to(c, commSearch.getSelDtKind(), dtTo));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // 범위 검색
        if (StringUtils.hasText(commSearch.getSelSection())) {
            // 범위 - From
            if (StringUtils.hasText(commSearch.getTxtSectionFrom())) {
                spec = addSpecification(Operation.AND,
                        spec,
                        commSpecification.from(c, commSearch.getSelSection(), commSearch.getTxtSectionFrom()));
            }
            // 범위 - To
            if (StringUtils.hasText(commSearch.getTxtSectionTo())) {
                spec = addSpecification(Operation.AND,
                        spec,
                        commSpecification.to(c, commSearch.getSelSection(), commSearch.getTxtSectionTo()));
            }
        }

        // 상세 검색
        if (StringUtils.hasText(commSearch.getSelDetail())) {
            spec = addSpecification(Operation.AND,
                    spec,
                    commSpecification.like(c, commSearch.getSelDetail(), commSearch.getTxtDetail()));
        }

        return spec;
    }

    protected <T> Specification<T> addSpecification(Operation operation, Specification<T> origin, Specification<T> spec) {
        if (origin == null) {
            return Specification.where(spec);
        } else {
            switch (operation) {
                case AND:
                    return origin.and(spec);
                case OR:
                    return origin.or(spec);
                default:
                    return Specification.where(spec);  // Impossible
            }
        }
    }
}
