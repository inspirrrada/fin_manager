package ua.com.alevel.facade;

import ua.com.alevel.dto.UserDTO;

import java.util.List;

public interface UserFacade {

    List<UserDTO> findAll();

    void create(UserDTO dto);

    void update(Long id, UserDTO dto);
}
