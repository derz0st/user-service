package user.service.repository;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;
import user.service.model.User;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Singleton
public class UserRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public UserRepository(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }

    @Transactional
    public User getById(Long id) {
        return (User) query("from usr where id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    private TypedQuery query(String queryText) {
        return entityManager.createQuery(queryText, User.class);
    }
    
}
