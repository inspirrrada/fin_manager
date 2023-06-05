package ua.com.alevel.service;

import ua.com.alevel.persistence.entity.User;

import java.util.Collection;

public interface UserService {

    void create(User user);

    void update(User user);

    User findById(Long id);

    Collection<User> findAll();

}
