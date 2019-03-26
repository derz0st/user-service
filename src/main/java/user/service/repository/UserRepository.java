package user.service.repository;

import user.service.model.User;

public interface UserRepository {
    User save(User user);
    User getById(Long id);
}
