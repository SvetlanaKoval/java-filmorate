package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.EmptyListException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;

    private final Comparator<Film> COMPARATOR = (film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size());

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getAll();
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Film deleteFilm(Film deletedFilm) {
        return filmStorage.delete(deletedFilm);
    }

    public Film getFilm(Long filmId) {
        return filmStorage.getById(filmId);
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        User user = userStorage.getById(userId);

        Set<Long> filmLikes = film.getLikes();
        if (!filmLikes.add(userId)) {
            log.error("Like from user with id - {} was already added", userId);
            throw new DuplicatedDataException("This like is already added");
        }

        Set<Long> favouriteFilmsId = user.getFavouriteFilmsId();
        favouriteFilmsId.add(filmId);

        film.setLikes(filmLikes);
        user.setFavouriteFilmsId(favouriteFilmsId);

        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        User user = userStorage.getById(userId);

        Set<Long> filmLikes = film.getLikes();
        if (filmLikes.isEmpty()) {
            log.info("No likes");
            throw new EmptyListException(String.format("Film %s does`t have any likes", film.getName()));
        }

        if (!filmLikes.remove(userId)) {
            log.error("Like from user with id - {} did`t found", userId);
            throw new NotFoundException(String.format("Like from  user - %s not found", user.getName()));
        }

        Set<Long> favouriteFilmsId = user.getFavouriteFilmsId();
        favouriteFilmsId.remove(filmId);

        film.setLikes(filmLikes);
        user.setFavouriteFilmsId(favouriteFilmsId);

        return film;
    }

    public List<Film> getPopularFilms(int limit) {

        return filmStorage.getAll().stream()
            .sorted(COMPARATOR)
            .limit(limit)
            .collect(Collectors.toList());
    }
}