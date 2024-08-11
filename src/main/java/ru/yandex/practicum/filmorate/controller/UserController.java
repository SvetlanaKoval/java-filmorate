package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.service.FilmLikesService;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FilmLikesService filmLikesService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        log.info("Getting all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        log.info("Getting user wieh id - {}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody NewUserRequest request) {
        log.info("Posting new user with login - {}", request.getLogin());
        return userService.createUser(request);
    }

    @PutMapping()
    public UserDTO updateUser(@Valid @RequestBody UpdateUserRequest request) {
        Long id = Optional.of(request.getId()).orElseThrow(() -> new ValidationException("Updating user has no id"));
        log.info("Updating user with id - {}", request.getName());
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id - {}", id);
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}/films")
    public Set<Long> getAllFilmsLikeByUserId(@PathVariable Long id) {
        log.info("Getting all films likes by userId - {}", id);
        return filmLikesService.getAllByUserId(id);
    }

}