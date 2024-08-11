package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;
import ru.yandex.practicum.filmorate.model.UserFriends;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserFriendsRowMapper implements RowMapper<UserFriends> {

    @Override
    public UserFriends mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserFriends userFriends = new UserFriends();
        userFriends.setUserId(rs.getLong("user_id"));
        userFriends.setFriendId(rs.getLong("friend_id"));
        userFriends.setStatus(FriendsStatus.valueOf(rs.getString("friendship_status")));

        return userFriends;
    }

}
