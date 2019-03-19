package user.service.service;

import user.service.controller.dto.UserDto;
import user.service.controller.request.CreateUserRequest;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Random;

@Singleton
public class UserService {

    private final HashMap<Long, UserDto> users = new HashMap<>();

    public UserDto createUser(CreateUserRequest request) {
        UserDto userDto = new UserDto(new Random().nextLong(), request.getName(), request.getEmail());
        users.put(userDto.getId(), userDto);
        return userDto;
    }

    public UserDto getUser(Long id) {
        return users.get(id);
    }
}
