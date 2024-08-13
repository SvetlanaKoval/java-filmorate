package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsRepository;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.UserFriends;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendsService {

    private final UserService userService;
    private final FriendsRepository friendsRepository;

    private List<Long> getAllFriendsIds(Long userId) {
        return new ArrayList<>(getAllFriendsWithStatus(userId).keySet());
    }

    public Map<Long, FriendsStatus> getAllFriendsWithStatus(Long userId) {
        userService.checkUserExists(userId);
        return friendsRepository.findAllFriendsByUserId(userId)
            .stream()
            .collect(Collectors.toMap(UserFriends::getFriendId, UserFriends::getStatus));
    }

    public List<UserDTO> getAllFriendsList(Long userId) {
        Map<Long, FriendsStatus> allFriendsWithStatus = getAllFriendsWithStatus(userId);
        return userService.getUsersByIds(allFriendsWithStatus.keySet()).stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }

    public Long addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new DuplicatedDataException("Your can not be friend for yourself");
        }

        userService.checkUserExists(friendId);

        if (isFriendExists(userId, friendId)) {
            log.error("Friend with id - {} was added earlier to user with id - {}", userId, friendId);
            throw new DuplicatedDataException(String.format("This friend is already in user with id %s friendship list", friendId));
        }

        return friendsRepository.save(userId, friendId);
    }

    public boolean deleteFriend(Long userId, Long friendId) {
        userService.checkUserExists(userId);
        userService.checkUserExists(friendId);

        return friendsRepository.delete(userId, friendId);
    }

    public boolean deleteAllFriendsByUser(Long userId) {
        return friendsRepository.deleteAllFriendsByUser(userId);
    }

    public List<UserDTO> getCommonFriends(Long userId, Long otherId) {
        List<Long> userFriends = getAllFriendsIds(userId);
        List<Long> otherUserFriends = getAllFriendsIds(otherId);

        userFriends.retainAll(otherUserFriends);

        return userService.getUsersByIds(new HashSet<>(userFriends)).stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }

    private boolean isFriendExists(Long userId, Long friendId) {
        List<Long> userFriendsList = getAllFriendsIds(userId);

        return !userFriendsList.isEmpty() && userFriendsList.contains(friendId);
    }

}