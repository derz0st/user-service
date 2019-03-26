package user.service.repository;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import user.service.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class UserRepositoryTest {
    private static UserRepository userRepository;
    @PersistenceContext
    private static EntityManager entityManager;
    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

    @Container
    private static PostgreSQLContainer postgresqlContainer = (PostgreSQLContainer) new PostgreSQLContainer()
            .withDatabaseName("user-db")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5000);

    @BeforeAll
    static void setupServer() {
        String jdbcUrl = postgresqlContainer.getJdbcUrl();
        
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class, 
                        PropertySource.of("test", singletonMap("datasources.default.url", jdbcUrl)));
        
        userRepository = server.getApplicationContext().getBean(UserRepository.class);
        entityManager = server.getApplicationContext().getBean(EntityManager.class);
    }

    @Test
    void save_whenRequiredParametersProvided_savesUser() {
        User expected = random.nextObject(User.class);
        userRepository.save(expected);
        
//        TODO: setup entity manager 
//        String selectQuery = String.format("from users where id = %s", expected.getId());
//        User actual = entityManager.createQuery(selectQuery, User.class).getSingleResult();
        
        User actual = userRepository.getById(expected.getId());

        assertThat(actual).isEqualTo(expected);
    }
}