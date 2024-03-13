package org.acme.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.User;



@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    // You can add custom query methods here if needed
}

