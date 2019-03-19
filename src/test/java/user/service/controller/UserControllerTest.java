package user.service.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import user.service.controller.dto.UserDto;
import user.service.controller.request.CreateUserRequest;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.*;

public class UserControllerTest {
    private static EmbeddedServer server;
    private static HttpClient client;
    private static EnhancedRandom random;

    @BeforeClass
    public static void setUp() {
        server = ApplicationContext.run(EmbeddedServer.class);
        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());
        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    @Test
    public void createUser_whenAllFieldsProvided_returnsCreatedUser() throws InvocationTargetException, IllegalAccessException {
        CreateUserRequest request = random.nextObject(CreateUserRequest.class);
        UserDto expected = new UserDto();

        BeanUtils.copyProperties(expected, request);

        UserDto actual = client.toBlocking()
                .retrieve(HttpRequest.POST("/api/users", request), UserDto.class);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
    }
}