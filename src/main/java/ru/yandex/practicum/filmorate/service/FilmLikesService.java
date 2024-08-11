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

    public Set<Long> getAllByUserId(Long userId) {
        return filmLikesRepository.findAllByUserId(userId).stream()
            .map(FilmLikes::getFilmId)
            .collect(Collectors.toSet());
    }

    public Set<Long> getAllByFilmId(Long userId) {
        return filmLikesRepository.findAllByFilmId(userId).stream()
            .map(FilmLikes::getFilmId)
            .collect(Collectors.toSet());
    }

    public Long addFilmLike(Long userId, Long filmId) {
        return filmLikesRepository.save(userId, filmId);
    }

    public boolean deleteFilmLike(Long userId, Long filmId) {
        return filmLikesRepository.delete(userId, filmId);
    }

    public boolean deleteAllLikesByUser(Long userId) {
        return filmLikesRepository.deleteAllByUser(userId);
    }

}