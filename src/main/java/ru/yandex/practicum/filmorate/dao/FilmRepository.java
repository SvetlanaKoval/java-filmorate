package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("userRepository")
public class FilmRepository extends BaseRepository<Film> {

    private static final String TABLE_NAME_FILMS = "films";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(BaseQueries.getAll(TABLE_NAME_FILMS));
    }

    public Optional<Film> findById(Long id) {
        return findOne(BaseQueries.getById(TABLE_NAME_FILMS), id);
    }

    public Film getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Фильм с ID: %d не найден", id)));
    }

    public Film save(Film film) {
        Long id = insert(
            BaseQueries.insert(TABLE_NAME_FILMS, "name", "description", "release_date", "duration", "rating_id"),
            film.getName(),
            film.getDescription(),
            Date.valueOf(film.getReleaseDate()),
            film.getDuration(),
            film.getRating().getId()
        );
        updateGenres(id, film.getGenres());

        return getById(id);
    }

    public Film update(Film film) {
        update(BaseQueries.update(TABLE_NAME_FILMS, "name", "description", "release_date", "duration", "rating_id"),
            film.getName(),
            film.getDescription(),
            Date.valueOf(film.getReleaseDate()),
            film.getDuration(),
            film.getRating().getId(),
            film.getId()
        );
        updateGenres(film.getId(), film.getGenres());

        return getById(film.getId());
    }

    private void updateGenres(Long filmId, List<Genre> genres) {
        delete(BaseQueries.deleteByField("film_genres", "film_id"), filmId);

        genres.stream()
            .distinct()
            .forEach(g -> update(
                BaseQueries.insert("film_genres", "film_id", "genre_id"),
                filmId,
                g.getId()
            ));
    }

    public boolean delete(Long id) {
        return delete(BaseQueries.delete(TABLE_NAME_FILMS), id);
    }

}