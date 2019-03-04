package com.thoughtworks.market.respository;

import com.thoughtworks.market.entity.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomRepositoryImpl implements CustomRepository {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Product> fileterProducts(String category, String brand, float maxPrice, float minPrice) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        final Root<Product> productRoot = query.from(Product.class);

        Predicate conjunction = criteriaBuilder.conjunction();

        if (category != null && !category.isEmpty()) {
            conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.equal(productRoot.get("category"), category));
        }

        if (brand != null && !brand.isEmpty()) {
            conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.equal(productRoot.get("brand"), brand));
        }

        if (maxPrice != 0) {
            conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.lessThan(productRoot.get("price"), maxPrice));
        }

        if (minPrice != 0) {
            conjunction = criteriaBuilder.and(conjunction, criteriaBuilder.greaterThan(productRoot.get("price"), minPrice));
        }

        query.where(conjunction);
        return entityManager.createQuery(query).getResultList();
    }
}
