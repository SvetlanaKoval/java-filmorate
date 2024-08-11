package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikesRowMapper implements RowMapper<FilmLikes> {
    @Override
    public FilmLikes mapRow(ResultSet rs, int rowNum) throws SQLException {
        FilmLikes filmLikes = new FilmLikes();
        filmLikes.setFilmId(rs.getLong("film_id"));
        filmLikes.setUserId(rs.getLong("user_id"));

        return filmLikes;
    }
}
