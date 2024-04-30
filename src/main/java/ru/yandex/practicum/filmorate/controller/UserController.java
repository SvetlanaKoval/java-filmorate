package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.error("id {} already in use", (user.getId()));
            throw new DuplicatedDataException("User with id " + user.getId() + " already exists");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getLastId());
        users.put(user.getId(), user);

        log.info("The new user has been added");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        Long newUserId = newUser.getId();
        if (newUserId == null) {
            log.error("Did`t find user for updating because you send user with id = null");
            throw new ValidateException("User should have an id");
        }
        User oldUser = users.get(newUserId);
        if (oldUser == null) {
            log.error("Did`n find user with id {}", newUserId);
            throw new ValidateException("Can`t find user with id " + newUserId);
        }
        if (!oldUser.getEmail().equals(newUser.getEmail())) {
            users.values().stream()
                .map(User::getEmail)
                .filter(email -> email.equals(newUser.getEmail()))
                .findFirst()
                .ifPresent(email -> {
                    log.error("Choose another email, email {} is already in use", email);
                    throw new DuplicatedDataException("User with email " + email + " is already exists");
                });
        }
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());

        log.info("The user has been updated");
        return oldUser;
    }

    private long getLastId() {
        long lastId = users.values().stream()
            .mapToLong(User::getId)
            .max()
            .orElse(0);
        log.trace("Generating new id");
        return ++lastId;
    }

}