package com.littletrips.tripsim.service;

import com.littletrips.tripsim.model.dto.User;
import com.littletrips.tripsim.service.db.UserLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserLoader userLoader;

    @InjectMocks
    private UserService userService;

    private User userA;
    private User userB;

    @BeforeEach
    void setUp() {
        userA = new User("5500005555555559", "Alice");
        userB = new User("4111111111111111", "Bob");
    }

    @Test
    void getUserByPAN_returnsMatchingUser() {
        when(userLoader.getUsers()).thenReturn(List.of(userA, userB));

        User result = userService.getUserByPAN("5500005555555559");

        assertThat(result).isEqualTo(userA);
        verify(userLoader, times(1)).getUsers();
    }

    @Test
    void getUserByPAN_returnsNullWhenNoMatch() {
        when(userLoader.getUsers()).thenReturn(List.of(userA, userB));

        User result = userService.getUserByPAN("9999999999999999");

        assertThat(result).isNull();
    }

    @Test
    void getUserByPAN_returnsNullWhenUserListIsEmpty() {
        when(userLoader.getUsers()).thenReturn(Collections.emptyList());

        User result = userService.getUserByPAN("5500005555555559");

        assertThat(result).isNull();
    }

    @Test
    void getUserByPAN_returnsFirstMatchWhenMultipleUsersSharePAN() {
        User duplicate = new User("5500005555555559", "Alice");
        when(userLoader.getUsers()).thenReturn(List.of(userA, duplicate, userB));

        User result = userService.getUserByPAN("5500005555555559");

        assertThat(result).isEqualTo(userA);
    }

    @Test
    void getUserByPAN_matchesExactPANOnly() {
        when(userLoader.getUsers()).thenReturn(List.of(userA, userB));

        User result = userService.getUserByPAN("550000555555555");

        assertThat(result).isNull();
    }
}