package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.FilmLikesService;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films/{id}/like")
public class FilmLikesController {

    private final FilmLikesService filmLikesService;

    @GetMapping
    public Set<Long> getAllByFilmId(@PathVariable Long id) {
        log.info("Getting all likes by filmId - {}", id);
        return filmLikesService.getAllByFilmId(id);
    }

    @PutMapping("/{userId}")
    public Long addFilmLike(@PathVariable Long userId, @PathVariable Long id) {
        log.info("Adding like to film with id - {} from user with id - {}", id, userId);
        return filmLikesService.addFilmLike(userId, id);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteFilmLike(@PathVariable Long userId, @PathVariable Long id) {
        log.info("Deleting like of film with id - {} from user with id - {}", id, userId);
        return filmLikesService.deleteFilmLike(userId, id);
    }

}