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

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setBirthdayDate(request.getBirthdayDate());
        user.setBio(request.getBio());

        user = userRepository.save(user);

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdayDate(),
                user.getBio()
        );
    }

    public UserDto getUser(Long id) {
        User user = userRepository.getById(id);
        
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthdayDate(),
                user.getBio()
        );
    }
}
