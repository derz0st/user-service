package user.service.repository.implementation;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;
import user.service.model.User;
import user.service.repository.UserRepository;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Singleton
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public UserRepositoryImpl(@CurrentSession EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setId(1L);
        entityManager.persist(user);
        return user;
    }

    @Override
    @Transactional
    public User getById(Long id) {
        return (User) query("from users u where u.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    private TypedQuery query(String queryText) {
        return entityManager.createQuery(queryText, User.class);
    }
}
