package com.littletrips.tripsim.controller;

import com.littletrips.tripsim.model.dto.User;
import com.littletrips.tripsim.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{pan}")
    public User getUserbyPAN (@PathVariable String pan) {
        return userService.getUserByPAN(pan);
    }
}
