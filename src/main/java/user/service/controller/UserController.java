package user.service.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import user.service.controller.dto.UserDto;
import user.service.controller.request.CreateUserRequest;
import user.service.service.UserService;

@Controller("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Post(processes = MediaType.APPLICATION_JSON)
    public UserDto createUser(@Body CreateUserRequest request) {
        return userService.createUser(request);
    }

    @Get("/{id}")
    public UserDto getUser(Long id) {
        return userService.getUser(id);
    }
}
