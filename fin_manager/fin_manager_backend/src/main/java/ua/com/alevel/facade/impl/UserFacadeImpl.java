package ua.com.alevel.facade.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ua.com.alevel.dto.UserDTO;
import ua.com.alevel.facade.UserFacade;
import ua.com.alevel.persistence.entity.User;
import ua.com.alevel.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    @Override
    public List<UserDTO> findAll() {
        Collection<User> users = userService.findAll();
        if (CollectionUtils.isNotEmpty(users)) {
            return users.stream().map(UserDTO::new).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public void create(UserDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFullName().split("\\s")[0]);
        user.setLastName(dto.getFullName().split("\\s")[1]);
        user.setAccountsQty(dto.getAccountsQty());
        userService.create(user);
    }

    @Override
    public void update(Long id, UserDTO dto) {
        User user = userService.findById(id);
        user.setFirstName(dto.getFullName().split("\\s")[0]);
        user.setLastName(dto.getFullName().split("\\s")[1]);
        user.setAccountsQty(dto.getAccountsQty());
        userService.update(user);
    }
}
