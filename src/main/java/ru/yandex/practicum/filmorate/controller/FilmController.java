package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.error("id {} already in use", (film.getId()));
            throw new DuplicatedDataException("Film with id " + film.getId() + " already exists");
        }
        film.setId(getLastId());
        films.put(film.getId(), film);

        log.info("The new film has been added");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        Long newFilmId = newFilm.getId();
        if (newFilmId == null) {
            log.error("Did`t find film for updating because you send film with id = null");
            throw new ValidateException("Film should have an id");
        }
        Film oldFilm = films.get(newFilmId);
        if (oldFilm == null) {
            log.error("Did`n find film with id {}", newFilmId);
            throw new ValidateException("Can`t find film with id " + newFilmId);
        }

        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setDuration(newFilm.getDuration());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());

        log.info("The film has been updated");
        return oldFilm;
    }

    private Long getLastId() {
        long lastId = films.values().stream()
            .mapToLong(Film::getId)
            .max()
            .orElse(0);
        log.trace("Generating new id");
        return ++lastId;
    }

}