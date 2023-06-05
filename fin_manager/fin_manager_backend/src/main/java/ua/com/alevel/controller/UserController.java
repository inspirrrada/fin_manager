package ua.com.alevel.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.com.alevel.dto.UserDTO;
import ua.com.alevel.facade.UserFacade;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @GetMapping
    public ResponseEntity<Collection<UserDTO>> findAll() {
        return ResponseEntity.ok(userFacade.findAll());
    }
}
