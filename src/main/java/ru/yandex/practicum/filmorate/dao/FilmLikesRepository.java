package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import java.util.List;

@Repository
public class FilmLikesRepository extends BaseRepository<FilmLikes> {

    private static final String TABLE_NAME_FILM_LIKES = "film_likes";

    private static final String FIND_ALL_BY_USER = String.format("SELECT * FROM %s WHERE user_id = ?", TABLE_NAME_FILM_LIKES);
    private static final String FIND_ALL_BY_FILM = String.format("SELECT * FROM %s WHERE film_id = ?", TABLE_NAME_FILM_LIKES);
    private static final String ADD_FILMS_LIKE = String.format("INSERT INTO %s (user_id, film_id) VALUES (?, ?)", TABLE_NAME_FILM_LIKES);
    private static final String DELETE_FILMS_LIKE = String.format("DELETE FROM %s WHERE user_id = ? AND film_id = ?", TABLE_NAME_FILM_LIKES);
    private static final String DELETE_USER = String.format("DELETE FROM %s WHERE user_id = ?", TABLE_NAME_FILM_LIKES);

    public FilmLikesRepository(JdbcTemplate jdbc, RowMapper<FilmLikes> mapper) {
        super(jdbc, mapper);
    }

    public List<FilmLikes> findAllByUserId(Long userId) {
        return jdbc.query(FIND_ALL_BY_USER, mapper, userId);
    }

    public List<FilmLikes> findAllByFilmId(Long filmId) {
        return jdbc.query(FIND_ALL_BY_FILM, mapper, filmId);
    }

    public Long save(Long userId, Long filmId) {
        update(ADD_FILMS_LIKE, userId, filmId);

        return userId;
    }

    public boolean delete(Long userId, Long filmId) {
        int rowsDeleted = jdbc.update(DELETE_FILMS_LIKE, userId, filmId);

        return rowsDeleted == 1;
    }

    public boolean deleteAllByUser(Long userId) {
        int rowsDeleted = jdbc.update(DELETE_USER, userId);

        return rowsDeleted > 0;
    }

}