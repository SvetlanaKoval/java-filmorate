package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.EmptyListException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public User deleteUser(User deletedUser) {
        return userStorage.deleteUser(deletedUser);
    }

    public User addFriend(Long userId, Long friendId) {
        log.info("Adding new friend");

        User user = getUser("User", userId);
        User friend = getUser("Friend", friendId);

        Set<Long> userFriends = user.getFriends();
        if (!userFriends.add(friendId)) {
            log.error("Friend with name - {} was added earlier", friend.getName());
            throw new DuplicatedDataException("This friend is already in your list");
        }

        user.setFriends(userFriends);

        Set<Long> friendFriends = friend.getFriends();
        friendFriends.add(userId);
        friend.setFriends(friendFriends);
        return friend;
    }

    public User removeFriend(Long userId, Long friendId) {
        log.info("Removing friend");

        User user = getUser("User", userId);
        User removedFriend = getUser("Friend", friendId);

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

        return removedFriend;
    }

    public List<User> getAllFriends(Long userId) {
        User user = getUser("User", userId);
        log.info("Getting all friends of {}", user.getName());

        return getUsersFromIds(user.getFriends());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        User user = getUser("User", userId);
        User otherUser = getUser("Another user", otherId);
        log.info("Getting common friends of {} and {}", user.getName(), otherUser.getName());

        Set<Long> commonFriends = findIntersectionUsersId(user.getFriends(), otherUser.getFriends());
        return getUsersFromIds(commonFriends);
    }

    User getUser(String role, Long id) {
        return userStorage.getUsers().stream()
            .filter(user -> user.getId().equals(id))
            .findFirst().orElseThrow(() ->
                new NotFoundException(String.format("%s with id - %d not found", role, id)));
    }

    private Set<Long> findIntersectionUsersId(Set<Long> collection1, Set<Long> collection2) {
        return collection1.stream()
            .filter(userId1 -> collection2.stream()
                .anyMatch(userId2 -> userId2.equals(userId1)))
            .collect(Collectors.toSet());
    }

    private List<User> getUsersFromIds(Set<Long> ids) {
        return userStorage.getUsers().stream()
            .filter(user -> ids.stream()
                .anyMatch(friend -> friend.equals(user.getId())))
            .collect(Collectors.toList());
    }
}
