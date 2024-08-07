package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Getting all films");
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Posting new film - {}", film.getName());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Updating film - {}", newFilm.getName());
        return filmService.updateFilm(newFilm);
    }

    @DeleteMapping
    public Film deleteFilm(@Valid @RequestBody Film deletedFilm) {
        log.info("Deleting film - {}", deletedFilm.getName());
        return filmService.deleteFilm(deletedFilm);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Adding new like to film with id - {} from user with id - {}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Deleting like from film with id - {} from user with id - {}", id, userId);
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Getting {} most popular films", count);
        return filmService.getPopularFilms(count);
    }
}