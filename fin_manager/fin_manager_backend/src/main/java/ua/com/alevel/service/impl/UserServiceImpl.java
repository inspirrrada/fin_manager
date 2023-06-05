package ua.com.alevel.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.persistence.repository.UserRepository;
import ua.com.alevel.service.UserService;

import java.util.Collection;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void create(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }
}
