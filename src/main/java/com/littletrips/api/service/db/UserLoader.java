package com.littletrips.api.service.db;

import com.littletrips.api.model.dto.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLoader {
    private final LocalDBLoader localDBLoader;

    @Autowired
    public UserLoader(LocalDBLoader localDBLoader) {
        this.localDBLoader = localDBLoader;
    }

    @Getter
    private List<User> users;

    @PostConstruct
    public void loadFareTable() {
        users = localDBLoader.load("json/users.json", User.class);
    }
}