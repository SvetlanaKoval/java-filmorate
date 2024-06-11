package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import java.util.Collection;

@Slf4j
@Component
public class InMemoryUserStorage extends Storage<User> {

    @Override
    public Collection<User> getAll() {
        return storage.values();
    }

    @Override
    public User create(User user) {
        if (storage.containsKey(user.getId())) {
            log.error("id {} already in use", (user.getId()));
            throw new DuplicatedDataException("User with id " + user.getId() + " already exists");
        }

        //Checking name, if null - name = login
        userNameValidation(user);
        //Checking email, if email of user is already exists
        userEmailValidation(user.getEmail());
        user.setId(++idGenerator);
        storage.put(user.getId(), user);

        log.info("The new user {} has been added", user.getName());
        return user;
    }

    @Override
    public User update(User newUser) {
        Long newUserId = newUser.getId();
        if (newUserId == null) {
            log.error("Did`t find user for updating because you send user with id = null");
            throw new ValidateException("User should have an id");
        }

        //Checking name, if null - name = login
        userNameValidation(newUser);

        User oldUser = storage.get(newUserId);
        if (oldUser == null) {
            log.error("Did`n find user with id {}", newUserId);
            throw new NotFoundException("Can`t find user with id " + newUserId);
        }
        //Checking email, if email of updating user is already exists
        if (!oldUser.getEmail().equals(newUser.getEmail())) {
            userEmailValidation(newUser.getEmail());
        }
        oldUser.setName(newUser.getName());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());

        log.info("The user {} has been updated", oldUser.getName());
        return oldUser;
    }

    @Override
    public User delete(User user) {
        Long deletedUserId = user.getId();
        User deletedUser = storage.get(deletedUserId);
        if (deletedUser == null) {
            log.error("Did`n find user with id {}", deletedUserId);
            throw new NotFoundException("Can`t find user with id " + deletedUserId);
        }

        storage.remove(deletedUserId);

        log.info("The user {} has been deleted", deletedUser.getName());
        return deletedUser;
    }

    @Override
    public User getById(Long id) {
        return storage.values().stream()
            .filter(user -> user.getId().equals(id))
            .findFirst().orElseThrow(() ->
                new NotFoundException(String.format("User with id - %d not found", id)));
    }

    private void userNameValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void userEmailValidation(String newEmail) {
        storage.values().stream()
            .map(User::getEmail)
            .filter(email -> email.equals(newEmail))
            .findFirst()
            .ifPresent(email -> {
                log.error("Choose another email, email {} is already in use", email);
                throw new DuplicatedDataException("User with email " + email + " is already exists");
            });
    }
}
