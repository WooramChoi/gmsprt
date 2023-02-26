package net.adonika.gmsprt.comm.impl;

import net.adonika.gmsprt.comm.CommRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public class CommRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements CommRepository<T, ID> {

    private final EntityManager entityManager;

    public CommRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<T> findAll(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, Sort.unsorted());
        query.setHint(entityGraphType.getKey(), entityManager.getEntityGraph(entityGraphName));
        return query.getResultList();
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, pageable.getSort());
        query.setHint(entityGraphType.getKey(), entityManager.getEntityGraph(entityGraphName));
        return readPage(query, getDomainClass(), pageable, spec);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort, EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, sort);
        query.setHint(entityGraphType.getKey(), entityManager.getEntityGraph(entityGraphName));
        return query.getResultList();
    }

    @Override
    public T findOne(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = getQuery(spec, Sort.unsorted());
        query.setHint(entityGraphType.getKey(), entityManager.getEntityGraph(entityGraphName));
        return query.getSingleResult();
    }
}
