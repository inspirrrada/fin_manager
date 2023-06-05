package ua.com.alevel.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.alevel.api.UserApiService;

@Controller
@RequestMapping(value = {"/users"})
@AllArgsConstructor
public class UserController {

    private final UserApiService userApiService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userApiService.findAll());
        return "pages/users";
    }
}
