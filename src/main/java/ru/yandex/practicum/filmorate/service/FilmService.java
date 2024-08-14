package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikesRepository;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.GenreRepository;
import ru.yandex.practicum.filmorate.dao.RatingRepository;
import ru.yandex.practicum.filmorate.dto.film.FilmDTO;
import ru.yandex.practicum.filmorate.dto.film.GenreDTO;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;
    private final FilmLikesRepository filmLikesRepository;

    public List<FilmDTO> getAllFilms() {
        return filmRepository.findAll().stream()
            .map(film -> getFilmWithRatingAndGenres(film.getId()))
            .map(FilmMapper::toDTO)
            .collect(Collectors.toList());
    }

    public FilmDTO getById(Long filmId) {
        Film filmById = getFilmWithRatingAndGenres(filmId);

        return FilmMapper.toDTO(filmById);
    }

    private Film getFilmWithRatingAndGenres(Long filmId) {
        Film filmById = filmRepository.getById(filmId);
        Rating rating = ratingRepository.getById(filmById.getRating().getId());
        List<Genre> genres = genreRepository.findGenresByFilmId(filmId);

        filmById.setRating(rating);
        filmById.setGenres(genres);
        return filmById;
    }

    public FilmDTO createFilm(NewFilmRequest request) {
        Rating rating = ratingRepository.getById(request.getMpa().getId());

        List<Genre> genres = request.getGenres().stream()
            .map(GenreDTO::getId)
            .collect(Collectors.collectingAndThen(Collectors.toList(), genreRepository::findAllByIds));
        if (request.getGenres().size() != genres.size()) {
            throw new ValidateException("Не найден жанр");
        }

        Film film = FilmMapper.toFilm(request, rating, genres);
        Film save = filmRepository.save(film);

        return FilmMapper.toDTO(save);
    }

    public FilmDTO updateFilm(UpdateFilmRequest request) {
        Film film = filmRepository.getById(request.getId());
        Rating rating = ratingRepository.getById(request.getMpa().getId());
        List<Genre> genres = request.getGenres().stream()
            .map(GenreDTO::getId)
            .collect(Collectors.collectingAndThen(Collectors.toList(), genreRepository::findAllByIds));

        Film updatedFilm = FilmMapper.updateFilmFields(film, request, rating, genres);

        filmRepository.update(updatedFilm);

        return FilmMapper.toDTO(updatedFilm);
    }

    public boolean deleteFilm(Long id) {
        return filmRepository.delete(id);
    }

    public List<FilmDTO> getPopularFilms(int limit) {
        return filmRepository.findPopularFilmsIds(limit).stream()
            .map(FilmMapper::toDTO)
            .toList();
    }

    public void checkFilmExists(Long filmId) {
        filmRepository.getById(filmId);
    }

}