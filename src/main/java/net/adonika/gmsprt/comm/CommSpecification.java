package net.adonika.gmsprt.comm;

import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class CommSpecification {

    public <T> Specification<T> from(Class<T> c, String column, String from) {
        // Java 8 부터 Lambda 표현식 사용 가능
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(column), from);
    }

    public <T> Specification<T> from(Class<T> c, String column, Date dtFrom) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(column), dtFrom);
    }

    public <T> Specification<T> to(Class<T> c, String column, String to) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(column), to);
    }

    public <T> Specification<T> to(Class<T> c, String column, Date dtTo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(column), dtTo);
    }

    public <T> Specification<T> like(Class<T> c, String column, String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(column), "%" + value + "%");
    }

    public <T> Specification<T> equal(Class<T> c, String column, String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(column), value);
    }

    public <T> Specification<T> notEqual(Class<T> c, String column, String value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(column), value);
    }

    /**
     * Use {@link #isTrue(Class, String)} or {@link #isFalse(Class, String)}
     */
    @Deprecated
    public <T> Specification<T> equal(Class<T> c, String column, boolean value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(column), value);
    }

    public <T> Specification<T> isTrue(Class<T> c, String column) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get(column));
    }

    public <T> Specification<T> isFalse(Class<T> c, String column) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get(column));
    }
}
