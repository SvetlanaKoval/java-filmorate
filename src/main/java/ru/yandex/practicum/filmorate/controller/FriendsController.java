package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.service.FriendsService;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/friends")
public class FriendsController {

    private final FriendsService userService;

    @GetMapping
    public List<UserDTO> getFriendsListByUserId(@PathVariable Long userId) {
        log.info("Getting friends of user with id - {}", userId);
        return userService.getAllFriendsList(userId);
    }

    @PutMapping("/{friendId}")
    public Long addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Adding friend with id - {} to user with id - {}", friendId, userId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{friendId}")
    public boolean removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Deleting friend with id - {} from friends list of user with id - {}", friendId, userId);
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/common/{otherId}")
    public List<UserDTO> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Getting common friends of users with id - {} and {}", userId, otherId);
        return userService.getCommonFriends(userId, otherId);
    }

}