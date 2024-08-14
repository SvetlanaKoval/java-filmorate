package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String TABLE_NAME_GENRES = "genres";
    private static final String TABLE_NAME_FILM_GENRES = "film_genres";

    private static final String FIND_GENRES_BY_FILM = String.format("""
        SELECT g.*
        FROM %s g
            JOIN %s fg
        ON g.id = fg.genre_id
        WHERE fg.film_id = ?""", TABLE_NAME_GENRES, TABLE_NAME_FILM_GENRES);
    private static final String DELETE_FILMS_GENRE = String.format("DELETE FROM %s WHERE film_id = ? AND genre_id = ?", TABLE_NAME_FILM_GENRES);
    private static final String DELETE_FILM = String.format("DELETE FROM %s WHERE film_id = ?", TABLE_NAME_FILM_GENRES);

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(BaseQueries.getAll(TABLE_NAME_GENRES));
    }

    public List<Genre> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return findMany(BaseQueries.getAllById(TABLE_NAME_GENRES, ids.size()), ids.toArray());
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

    public boolean delete(Long filmId, Long genreId) {
        int rowsDeleted = jdbc.update(DELETE_FILMS_GENRE, filmId, genreId);

        return rowsDeleted == 1;
    }

    public void updateAllByFilm(long filmId, List<Genre> genres) {
        jdbc.update(DELETE_FILM, filmId);
        if (genres.isEmpty()) {
            return;
        }

        String filmToGenreParameters = genres.stream()
            .map(genre -> String.format("(%s, %s)", filmId, genre.getId()))
            .collect(Collectors.joining(", "));

        String query = String.format("INSERT INTO film_genres (film_id, genre_id) VALUES %s", filmToGenreParameters);

        update(query);
    }

}