package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;
import ru.yandex.practicum.filmorate.model.UserFriends;
import java.util.List;

@Repository
public class FriendsRepository extends BaseRepository<UserFriends> {

    private static final String TABLE_NAME_FRIENDS = "user_friends";

    private static final String FIND_USER_FRIENDS = String.format("SELECT * FROM %s WHERE user_id = ?", TABLE_NAME_FRIENDS);
    private static final String ADD_NEW_FRIEND =
        String.format("INSERT INTO %s (user_id, friend_id, friendship_status) VALUES (?, ?, ?)", TABLE_NAME_FRIENDS);
    private static final String UPDATE_FRIEND_STATUS =
        String.format("UPDATE %s SET friendship_status = ? WHERE user_id = ? AND friend_id = ?", TABLE_NAME_FRIENDS);
    private static final String DELETE_USER_FRIEND = String.format("DELETE FROM %s WHERE user_id = ? AND friend_id = ?", TABLE_NAME_FRIENDS);
    private static final String DELETE_USER = String.format("DELETE FROM %s WHERE user_id = ?", TABLE_NAME_FRIENDS);
    private static final String DELETE_USERS_BY_FRIEND = String.format("DELETE FROM %s WHERE friend_id = ?", TABLE_NAME_FRIENDS);

    public FriendsRepository(JdbcTemplate jdbc, RowMapper<UserFriends> mapper) {
        super(jdbc, mapper);
    }

    public List<UserFriends> findAllFriendsByUserId(Long userId) {
        return jdbc.query(FIND_USER_FRIENDS, mapper, userId);
    }

    public Long save(Long userId, Long friendId) {
        update(ADD_NEW_FRIEND,
            userId,
            friendId,
            FriendsStatus.UNCONFIRMED.name());

        return userId;
    }

    public void updateStatus(Long userId, Long friendId, String status) {
        update(UPDATE_FRIEND_STATUS, status, userId, friendId);
    }

    public boolean delete(Long userId, Long friendId) {
        int rowsDeleted = jdbc.update(DELETE_USER_FRIEND, userId, friendId);

        return rowsDeleted == 1;
    }

    public boolean deleteAllFriendsByUser(Long userId) {
        int rowsDeleted = jdbc.update(DELETE_USER, userId);

        return rowsDeleted > 0;
    }

    public boolean deleteAllUsersByFriend(Long friendId) {
        int rowsDeleted = jdbc.update(DELETE_USERS_BY_FRIEND, friendId);

        return rowsDeleted > 0;
    }

}