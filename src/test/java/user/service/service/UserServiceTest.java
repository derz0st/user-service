package user.service.service;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.service.controller.dto.UserDto;
import user.service.controller.request.CreateUserRequest;
import user.service.model.User;
import user.service.repository.UserRepository;

import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService subject;
    
    private EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder().build();
    
    @Test
    void createUser_whenAllParametersProvided_createsUserDtoAndReturnsIt() throws InvocationTargetException, IllegalAccessException {
        User user = random.nextObject(User.class, "id");
        UserDto expected = new UserDto();
        CreateUserRequest request = new CreateUserRequest();
        
        BeanUtils.copyProperties(request, user);
        BeanUtils.copyProperties(expected, user);

        when(userRepository.save(any(User.class))).thenReturn(user);
        
        UserDto actual = subject.createUser(request);
        
        verify(userRepository).save(user);
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
    }
    
}