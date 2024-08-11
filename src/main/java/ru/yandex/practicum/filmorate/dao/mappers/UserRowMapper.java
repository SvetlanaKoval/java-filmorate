package ru.yandex.practicum.filmorate.dao.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FriendsStatus;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserFriends;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Qualifier("userMapper")
@RequiredArgsConstructor
public class UserRowMapper implements RowMapper<User> {

    private final JdbcTemplate jdbc;
    private final UserFriendsRowMapper userFriendsRowMapper;
    private final FilmLikesRowMapper filmLikesRowMapper;

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        Long userId = rs.getLong("id");
        user.setId(userId);
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        Date birthday = rs.getDate("birthday");
        user.setBirthday(birthday.toLocalDate());

        List<UserFriends> userFriends = jdbc.query("SELECT * FROM user_friends WHERE user_id = ?", userFriendsRowMapper, userId);

        Map<Long, FriendsStatus> friendsStatusMap = userFriends.stream()
            .collect(Collectors.toMap(UserFriends::getFriendId, UserFriends::getStatus));
        user.setFriends(friendsStatusMap);

        List<FilmLikes> filmLikes = jdbc.query("SELECT * FROM film_likes WHERE user_id = ?", filmLikesRowMapper, userId);

        Set<Long> favouriteFilms = filmLikes.stream()
            .map(FilmLikes::getFilmId)
            .collect(Collectors.toSet());
        user.setFavouriteFilmsId(favouriteFilms);

        return user;
    }

}