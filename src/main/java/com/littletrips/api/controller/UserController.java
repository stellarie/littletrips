package com.littletrips.api.controller;

import com.littletrips.api.controller.exception.ExceptionMessage;
import com.littletrips.api.model.dto.User;
import com.littletrips.api.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{pan}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserbyPAN (@PathVariable String pan) {
        return userService.getUserByPAN(pan);
    }

    @ExceptionHandler(exception = Exception.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleException() {
        return ResponseEntity
                .internalServerError()
                .body(ExceptionMessage.getGenericExceptionMessage());
    }
}
