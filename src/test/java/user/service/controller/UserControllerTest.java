package user.service.controller;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import user.service.controller.dto.UserDto;
import user.service.controller.request.CreateUserRequest;
import user.service.repository.UserRepository;
import user.service.service.UserService;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private static EmbeddedServer server;
    private static UserService userService;
    private static HttpClient client;
    private static EnhancedRandom random;

    @BeforeAll
    static void setUp() {
        userService = mock(UserService.class);
        server = ApplicationContext
                .build()
                .build()
                .registerSingleton(userService)
                .registerSingleton(mock(UserRepository.class))
                .start()
                .getBean(EmbeddedServer.class)
                .start();

        client = server
                .getApplicationContext()
                .createBean(HttpClient.class, server.getURL());

        random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
    }

    @AfterAll
    static void tearDown() {
        if (server != null) server.stop();
        if (client != null) client.stop();
    }

    @Test
    void createUser_whenAllFieldsProvided_returnsCreatedUser() throws InvocationTargetException, IllegalAccessException {
        CreateUserRequest request = random.nextObject(CreateUserRequest.class);
        UserDto expected = new UserDto();

        BeanUtils.copyProperties(expected, request);

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(expected);
        UserDto actual = client.toBlocking()
                .retrieve(HttpRequest.POST("/api/users", request), UserDto.class);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }
}