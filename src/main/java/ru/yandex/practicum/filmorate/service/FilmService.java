package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.EmptyListException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public Film deleteFilm(Film deletedFilm) {
        return filmStorage.deleteFilm(deletedFilm);
    }

    public Film addLike(Long filmId, Long userId) {
        log.info("Adding like");
        Film film = getFilm(filmId);
        User user = userService.getUser("User", userId);

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
        log.info("Removing like");
        Film film = getFilm(filmId);
        User user = userService.getUser("User", userId);

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
        log.info("Most popular films");
        return filmStorage.getFilms().stream()
            .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    private Film getFilm(Long filmId) {
        return filmStorage.getFilms().stream()
            .filter(film -> film.getId().equals(filmId))
            .findFirst().orElseThrow(() ->
                new NotFoundException(String.format("Film with id - %d not found", filmId))
            );
    }
}
