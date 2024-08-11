package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriends;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    public List<Long> getAllFriendsIds(Long userId) {
        return new ArrayList<>(getAllFriendsWithStatus(userId).keySet());
    }

    public Map<Long, FriendsStatus> getAllFriendsWithStatus(Long userId) {
        return friendsRepository.findAllFriendsByUserId(userId)
            .stream()
            .collect(Collectors.toMap(UserFriends::getFriendId, UserFriends::getStatus));
    }

    public List<UserDTO> getAllFriendsList(Long userId) {
        userRepository.getById(userId);
        Map<Long, FriendsStatus> allFriendsWithStatus = getAllFriendsWithStatus(userId);
        return userRepository.findAllByIds(allFriendsWithStatus.keySet()).stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }

    public Long addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new DuplicatedDataException("Your can not be friend for yourself");
        }

        if (isFriendExists(userId, friendId)) {
            log.error("Friend with id - {} was added earlier to user with id - {}", userId, friendId);
            throw new DuplicatedDataException(String.format("This friend is already in user with id %s friendship list", friendId));
        }

        return friendsRepository.save(userId, friendId);
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        userRepository.getById(userId);
        userRepository.getById(friendId);

        return friendsRepository.delete(userId, friendId);
    }

    public boolean deleteAllFriendsByUser(Long userId) {
        return friendsRepository.deleteAllFriendsByUser(userId);
    }

    public boolean deleteAllUsersByFriend(Long friendId) {
        return friendsRepository.deleteAllUsersByFriend(friendId);
    }

    public void updateFriendshipStatus(Long userId, Long friendId, FriendsStatus status) {
        friendsRepository.updateStatus(userId, friendId, status.name());
    }

    public List<UserDTO> getCommonFriends(Long userId, Long otherId) {
        User user = userRepository.getById(userId);
        User otherUser = userRepository.getById(otherId);

        Set<Long> friendsIntersections = user.getFriends().keySet().stream()
            .filter(friendId -> otherUser.getFriends().containsKey(friendId))
            .collect(Collectors.toSet());

        return userRepository.findAllByIds(friendsIntersections).stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }

    private boolean isFriendExists(Long userId, Long friendId) {
        User userById = userRepository.getById(userId);
        userRepository.getById(friendId);

        return userById.getFriends().keySet().stream()
            .anyMatch(friendId::equals);
    }

}