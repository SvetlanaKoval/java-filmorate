package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikesRepository;
import ru.yandex.practicum.filmorate.dao.FriendsRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UserDTO;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final FilmLikesRepository filmLikesRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }

    public UserDTO createUser(NewUserRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new ValidateException("Имейл должен быть указан");
        }

        Optional<User> alreadyExistUser = userRepository.findByEmail(request.getEmail());

        if (alreadyExistUser.isPresent()) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        User user = UserMapper.toUser(request);

        user = userRepository.save(user);

        return UserMapper.toDTO(user);
    }

    public UserDTO updateUser(Long userId, UpdateUserRequest request) {
        User updatedUser = userRepository.getById(userId);
        UserMapper.updateUserFields(updatedUser, request);
        userRepository.update(updatedUser);

        return UserMapper.toDTO(updatedUser);
    }

    public boolean deleteUser(Long id) {
        friendsRepository.deleteAllFriendsByUser(id);
        friendsRepository.deleteAllUsersByFriend(id);
        filmLikesRepository.deleteAllByUser(id);
        return userRepository.delete(id);
    }

    public UserDTO getUserById(Long id) {
        return UserMapper.toDTO(userRepository.getById(id));
    }

    public List<User> getUsersByIds(Set<Long> ids) {
        return userRepository.findAllByIds(ids);
    }

    public void checkUserExists(Long userId) {
        userRepository.getById(userId);
    }

}