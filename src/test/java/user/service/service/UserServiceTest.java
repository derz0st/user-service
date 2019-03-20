package user.service.service;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.micronaut.test.annotation.MicronautTest;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;
import user.service.controller.dto.UserDto;
import user.service.controller.request.CreateUserRequest;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class UserServiceTest {
    @Inject
    private UserService subject;
    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();

    @Test
    void createUser_whenAllParametersProvided_createsUserDtoAndReturnsIt() throws InvocationTargetException, IllegalAccessException {
        CreateUserRequest request = random.nextObject(CreateUserRequest.class);
        UserDto expected = new UserDto();
        BeanUtils.copyProperties(expected, request);

        UserDto actual = subject.createUser(request);

        assertThat(actual).isEqualToIgnoringNullFields(expected);
    }
}