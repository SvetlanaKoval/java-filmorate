package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikesRepository;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmLikesService {

    private final FilmLikesRepository filmLikesRepository;
    private final FilmService filmService;
    private final UserService userService;

    public Set<Long> getAllByUserId(Long userId) {
        userService.checkUserExists(userId);
        return filmLikesRepository.findAllByUserId(userId).stream()
            .map(FilmLikes::getFilmId)
            .collect(Collectors.toSet());
    }

    public Set<Long> getAllByFilmId(Long filmId) {
        filmService.checkFilmExists(filmId);
        return filmLikesRepository.findAllByFilmId(filmId).stream()
            .map(FilmLikes::getFilmId)
            .collect(Collectors.toSet());
    }

    public Long addFilmLike(Long userId, Long filmId) {
        userService.checkUserExists(userId);
        filmService.checkFilmExists(filmId);

        return filmLikesRepository.save(userId, filmId);
    }

    public boolean deleteFilmLike(Long userId, Long filmId) {
        userService.checkUserExists(userId);
        filmService.checkFilmExists(filmId);

        return filmLikesRepository.delete(userId, filmId);
    }

}