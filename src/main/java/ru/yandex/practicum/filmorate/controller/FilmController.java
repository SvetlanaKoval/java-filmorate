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
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<FilmDTO> getAllFilms() {
        log.info("Getting all films");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmDTO getById(@PathVariable Long id) {
        log.info("Getting film by id {}", id);
        return filmService.getById(id);
    }

    @PostMapping
    public FilmDTO createFilm(@Valid @RequestBody NewFilmRequest request) {
        log.info("Posting new film - {}", request.getName());
        return filmService.createFilm(request);
    }

    @PutMapping
    public FilmDTO updateFilm(@Valid @RequestBody UpdateFilmRequest request) {
        log.info("Updating film - {}", request.getName());
        return filmService.updateFilm(request);
    }

    @DeleteMapping("/{id}")
    public boolean deleteFilm(@PathVariable Long id) {
        log.info("Deleting film with id - {}", id);
        return filmService.deleteFilm(id);
    }

    @GetMapping("/popular")
    public List<FilmDTO> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Getting {} most popular films", count);
        return filmService.getPopularFilms(count);
    }

}