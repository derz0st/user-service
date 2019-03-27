package user.service.repository;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.PropertySource;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import user.service.model.User;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
class UserRepositoryTest {
    private static UserRepository userRepository;
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
    }

    @Test
    void save_whenAllAttributesProvided_savesUser() {
        User expected = userRepository.save(random.nextObject(User.class, "id"));
        
        User actual = userRepository.getById(expected.getId());
        
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"firstName", "lastName", "email"})
    void save_whenRequiredAttributeIsNotProvided_throwsException(String requiredAttribute) {
        User user = random.nextObject(User.class, "id", requiredAttribute);
        
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }
}