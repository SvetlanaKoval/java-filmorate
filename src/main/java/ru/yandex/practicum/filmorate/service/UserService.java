package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.EmptyListException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final Storage<User> userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User updateUser(User newUser) {
        return userStorage.update(newUser);
    }

    public User deleteUser(User deletedUser) {
        return userStorage.delete(deletedUser);
    }

    public User getUser(Long id) {
        return userStorage.getById(id);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        Set<Long> userFriends = user.getFriends();
        if (!userFriends.add(friendId)) {
            log.error("Friend with name - {} was added earlier", friend.getName());
            throw new DuplicatedDataException("This friend is already in your list");
        }

        user.setFriends(userFriends);

        Set<Long> friendFriends = friend.getFriends();
        friendFriends.add(userId);
        friend.setFriends(friendFriends);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User removedFriend = getUser(friendId);

        Set<Long> userFriends = user.getFriends();
        Set<Long> removedFriendFriends = removedFriend.getFriends();

        if (userFriends.isEmpty()) {
            log.info("No friends");
            throw new EmptyListException(String.format("User %s does`t have any friends", user.getName()));
        }

        if (!userFriends.remove(friendId)) {
            log.error("Friend with name - {} did`t found in {}`s friends list", removedFriend.getName(), user.getName());
            throw new NotFoundException(String.format("This friend not found in %s `s friend list", user.getName()));
        }

        removedFriendFriends.remove(userId);
        user.setFriends(userFriends);
        removedFriend.setFriends(removedFriendFriends);

        return user;
    }

    public List<User> getAllFriends(Long userId) {
        User user = getUser(userId);
        return getUsersFromIds(user.getFriends());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getUser(userId);
        User otherUser = getUser(otherId);

        Set<Long> commonFriends = findIntersectionUsersId(user.getFriends(), otherUser.getFriends());
        return getUsersFromIds(commonFriends);
    }

    private Set<Long> findIntersectionUsersId(Set<Long> collection1, Set<Long> collection2) {
        return collection1.stream()
            .filter(userId1 -> collection2.stream()
                .anyMatch(userId2 -> userId2.equals(userId1)))
            .collect(Collectors.toSet());
    }

    private List<User> getUsersFromIds(Set<Long> ids) {
        return userStorage.getAll().stream()
            .filter(user -> ids.stream()
                .anyMatch(friend -> friend.equals(user.getId())))
            .collect(Collectors.toList());
    }
}