package net.adonika.gmsprt.comm;

import java.text.ParseException;
import java.util.Date;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import net.adonika.gmsprt.comm.model.CommForm;
import net.adonika.gmsprt.util.DateUtil;

public class CommSpecificationBuilder {

    public enum Operation {
        AND, OR
    }

    public <T> Specification<T> build(Class<T> c, CommForm searchForm) {

        Specification<T> spec = null;
        CommSpecification commSpecification = new CommSpecification();

        // 날짜 검색
        if (StringUtils.hasText(searchForm.getSelDtKind())) {
            // 날짜 - From
            if (StringUtils.hasText(searchForm.getTxtDtFrom())) {
                try {
                    Date dtFrom = DateUtil.stringToDate(searchForm.getTxtDtFrom() + "000000", "yyyyMMddHHmmss");
                    spec = addSpecification(Operation.AND,
                            spec,
                            commSpecification.from(c, searchForm.getSelDtKind(), dtFrom));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // 날짜 - To
            if (StringUtils.hasText(searchForm.getTxtDtTo())) {
                try {
                    Date dtTo = DateUtil.stringToDate(searchForm.getTxtDtTo() + "000000", "yyyyMMddHHmmss");
                    spec = addSpecification(Operation.AND,
                            spec,
                            commSpecification.to(c, searchForm.getSelDtKind(), dtTo));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // 범위 검색
        if (StringUtils.hasText(searchForm.getSelSection())) {
            // 범위 - From
            if (StringUtils.hasText(searchForm.getTxtSectionFrom())) {
                spec = addSpecification(Operation.AND,
                        spec,
                        commSpecification.from(c, searchForm.getSelSection(), searchForm.getTxtSectionFrom()));
            }
            // 범위 - To
            if (StringUtils.hasText(searchForm.getTxtSectionTo())) {
                spec = addSpecification(Operation.AND,
                        spec,
                        commSpecification.to(c, searchForm.getSelSection(), searchForm.getTxtSectionTo()));
            }
        }

        // 상세 검색
        if (StringUtils.hasText(searchForm.getSelDetail())) {
            spec = addSpecification(Operation.AND,
                    spec,
                    commSpecification.like(c, searchForm.getSelDetail(), searchForm.getTxtDetail()));
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
