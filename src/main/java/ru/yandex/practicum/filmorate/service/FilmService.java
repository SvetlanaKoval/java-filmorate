package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.GenreRepository;
import ru.yandex.practicum.filmorate.dao.RatingRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;

    private static final Comparator<Film> FILM_LIKES_COMPARATOR = (film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size());

    public List<FilmDTO> getAllFilms() {
        return filmRepository.findAll().stream()
            .map(FilmMapper::toDTO)
            .collect(Collectors.toList());
    }

    public FilmDTO getById(Long filmId) {
        Film filmById = filmRepository.getById(filmId);
        return FilmMapper.toDTO(filmById);
    }

    public FilmDTO createFilm(NewFilmRequest request) {
        Rating rating = ratingRepository.getById(request.getMpa().getId());
        List<Genre> genres = request.getGenres().stream()
            .map(genre -> genreRepository.findById(genre.getId()))
            .map(genre -> genre.orElseThrow(() -> new ValidateException("Жанр не найден")))
            .toList();
        Film film = FilmMapper.toFilm(request, rating, genres);
        Film save = filmRepository.save(film);

        return FilmMapper.toDTO(save);
    }

    public FilmDTO updateFilm(UpdateFilmRequest request) {
        Film film = filmRepository.getById(request.getId());
        Rating rating = ratingRepository.getById(request.getMpa().getId());
        List<Genre> genres = request.getGenres().stream()
            .map(genre -> genreRepository.getById(genre.getId()))
            .toList();

        Film updatedFilm = FilmMapper.updateFilmFields(film, request, rating, genres);

        filmRepository.update(updatedFilm);

        return FilmMapper.toDTO(updatedFilm);
    }

    public boolean deleteFilm(Long id) {
        return filmRepository.delete(id);
    }

    public List<FilmDTO> getPopularFilms(int limit) {
        return filmRepository.findAll().stream()
            .sorted(FILM_LIKES_COMPARATOR)
            .limit(limit)
            .map(FilmMapper::toDTO)
            .collect(Collectors.toList());
    }
}