package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("genreRepository")
public class GenreRepository extends BaseRepository<Genre> {
    private static final String TABLE_NAME_GENRES = "genres";
    private static final String TABLE_NAME_FILM_GENRES = "film_genres";

    private static final String FIND_GENRES_BY_FILM = String.format("""
            SELECT g.*
            FROM films f
                     JOIN %s fg ON f.id = fg.film_id
                     JOIN %s g ON fg.genre_id = g.ID
            WHERE f.id = ?""",
        TABLE_NAME_FILM_GENRES, TABLE_NAME_GENRES);

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(BaseQueries.getAll(TABLE_NAME_GENRES));
    }

    public Optional<Genre> findById(Long id) {
        return findOne(BaseQueries.getById(TABLE_NAME_GENRES), id);
    }

    public Genre getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Жанр с ID: %d не найден", id)));
    }

    public List<Genre> findGenresByFilmId(Long filmId) {
        return findMany(FIND_GENRES_BY_FILM, filmId);
    }

}