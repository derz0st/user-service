package user.service.service;

import user.service.controller.dto.UserDto;
import user.service.controller.request.CreateUserRequest;
import user.service.model.User;
import user.service.repository.UserRepository;

import javax.inject.Singleton;

@Singleton
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user = userRepository.save(user);

        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public UserDto getUser(Long id) {
        User user = userRepository.getById(id);
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
