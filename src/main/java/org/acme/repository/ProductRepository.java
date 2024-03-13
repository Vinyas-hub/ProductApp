package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.acme.entity.Product;

import java.util.List;


@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    @PersistenceContext
    EntityManager entityManager;

    public List<Product> searchProductsByName(String name) {
        Query query = entityManager.createQuery("SELECT p FROM Product p WHERE p.name LIKE :name");
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }
}