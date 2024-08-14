package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.BaseIntegrationTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GenreRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void testFindAll() {
        List<Genre> genres = genreRepository.findAll();

        assertThat(genres)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(genre -> assertThat(genre).hasNoNullFieldsOrProperties());
    }

    @Test
    void testFindById_GenreExists() {
        Optional<Genre> genreOptional = genreRepository.findById(1L);

        assertThat(genreOptional)
            .isPresent()
            .hasValueSatisfying(genre -> {
                assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
                assertThat(genre.getName()).isEqualTo("Комедия");
            });
    }

    @Test
    void testFindById_GenreDoesNotExist() {
        Optional<Genre> genreOptional = genreRepository.findById(999L);

        assertThat(genreOptional).isNotPresent();
    }

    @Test
    void testGetById_GenreExists() {
        Genre genre = genreRepository.getById(1L);

        assertThat(genre)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void testGetById_GenreDoesNotExist() {
        assertThatThrownBy(() -> genreRepository.getById(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Жанр с ID: 999 не найден");
    }

    @Test
    void testFindGenresByFilmId() {
        List<Genre> genres = genreRepository.findGenresByFilmId(1L);

        assertThat(genres)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(genre -> assertThat(genre).hasNoNullFieldsOrProperties());
    }

}