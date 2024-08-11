package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.BaseIntegrationTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FilmRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private FilmRepository filmRepository;

    @Test
    void testFindAll() {
        List<Film> films = filmRepository.findAll();

        assertThat(films)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(film -> assertThat(film).hasNoNullFieldsOrProperties());
    }

    @Test
    void testFindById_FilmExists() {
        Optional<Film> filmOptional = filmRepository.findById(1L);

        assertThat(filmOptional)
            .isPresent()
            .hasValueSatisfying(film -> {
                assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                assertThat(film.getName()).isEqualTo("Film One");
            });
    }

    @Test
    void testFindById_FilmDoesNotExist() {
        Optional<Film> filmOptional = filmRepository.findById(999L);

        assertThat(filmOptional).isNotPresent();
    }

    @Test
    void testGetById_FilmExists() {
        Film film = filmRepository.getById(1L);

        assertThat(film)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "Film One");
    }

    @Test
    void testGetById_FilmDoesNotExist() {
        assertThatThrownBy(() -> filmRepository.getById(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Фильм с ID: 999 не найден");
    }

    @Test
    void testSave() {
        Film film = new Film();
        film.setName("New Film");
        film.setDescription("Description of new film");
        film.setReleaseDate(LocalDate.of(2024, 1, 1));
        film.setDuration(120);
        film.setRating(new Rating(1L, "PG"));
        film.setGenres(List.of(new Genre(1L, "Action"), new Genre(2L, "Adventure")));

        Film savedFilm = filmRepository.save(film);

        assertThat(savedFilm)
            .isNotNull()
            .hasFieldOrPropertyWithValue("name", "New Film")
            .hasFieldOrPropertyWithValue("description", "Description of new film")
            .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2024, 1, 1))
            .hasFieldOrPropertyWithValue("duration", 120)
            .hasFieldOrPropertyWithValue("rating.id", 1L)
            .hasFieldOrPropertyWithValue("genres", List.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")));

        assertThat(savedFilm.getId()).isNotNull();
    }

    @Test
    void testUpdate() {
        Film film = filmRepository.getById(1L);
        film.setName("Updated Film");

        Film updatedFilm = filmRepository.update(film);

        assertThat(updatedFilm)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "Updated Film");
    }

    @Test
    void testDelete_FilmExists() {
        boolean deleted = filmRepository.delete(1L);

        assertThat(deleted).isTrue();
    }

    @Test
    void testDelete_FilmDoesNotExist() {
        boolean deleted = filmRepository.delete(999L);

        assertThat(deleted).isFalse();
    }

}