package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String TABLE_NAME_FILMS = "films";

    private static final String FIND_ALL_POPULAR_FILMS = """
        SELECT f.*
        FROM film_likes fl
                 LEFT JOIN films f on f.id = fl.film_id
        GROUP BY f.id
        ORDER BY count(fl.user_id) DESC
        LIMIT ?""";

    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, RatingRepository ratingRepository, GenreRepository genreRepository) {
        super(jdbc, mapper);
        this.ratingRepository = ratingRepository;
        this.genreRepository = genreRepository;
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

    public List<Film> findAllByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return findMany(BaseQueries.getAllById(TABLE_NAME_FILMS, ids.size()), ids.toArray());
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
        genreRepository.updateAllByFilm(id, film.getGenres());

        Film savedFilm = getById(id);
        savedFilm.setRating(ratingRepository.getById(film.getRating().getId()));
        savedFilm.setGenres(genreRepository.findGenresByFilmId(id));

        return savedFilm;
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
        genreRepository.updateAllByFilm(film.getId(), film.getGenres());

        Film updatedFilm = getById(film.getId());
        updatedFilm.setRating(ratingRepository.getById(film.getRating().getId()));
        updatedFilm.setGenres(genreRepository.findGenresByFilmId(film.getId()));

        return updatedFilm;
    }

    public boolean delete(Long id) {
        return delete(BaseQueries.delete(TABLE_NAME_FILMS), id);
    }

    public List<Film> findPopularFilmsIds(int limit) {
        return jdbc.query(FIND_ALL_POPULAR_FILMS, mapper, limit);
    }

}