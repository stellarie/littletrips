package com.littletrips.tripsim.service;

import com.littletrips.tripsim.model.dto.User;
import com.littletrips.tripsim.service.db.UserLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserLoader userLoader;

    public UserService(UserLoader userLoader) {
        this.userLoader = userLoader;
    }

    public User getUserByPAN(String pan) {
        List<User> users = userLoader.getUsers();
        return users.stream().filter(user -> user.pan().equals(pan)).findFirst().orElse(null);
    }
}
