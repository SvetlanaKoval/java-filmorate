package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;

@Data
public class UserFriends {

    private Long userId;
    private Long friendId;
    private FriendsStatus status = FriendsStatus.UNCONFIRMED;
}