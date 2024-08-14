package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.BaseIntegrationTest;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

public class FilmLikesRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private FilmLikesRepository filmLikesRepository;

    @Test
    void testFindAllByUserId() {
        List<FilmLikes> filmLikes = filmLikesRepository.findAllByUserId(1L);

        assertThat(filmLikes)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(like -> assertThat(like).hasNoNullFieldsOrProperties());
    }

    @Test
    void testFindAllByFilmId() {
        List<FilmLikes> filmLikes = filmLikesRepository.findAllByFilmId(1L);

        assertThat(filmLikes)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(like -> assertThat(like).hasNoNullFieldsOrProperties());
    }

    @Test
    void testSave() {
        Long userId = 1L;
        Long filmId = 2L;

        Long savedUserId = filmLikesRepository.save(userId, filmId);

        assertThat(savedUserId).isEqualTo(userId);
    }

    @Test
    void testDelete() {
        Long userId = 1L;
        Long filmId = 2L;

        boolean deleted = filmLikesRepository.delete(userId, filmId);

        assertThat(deleted).isTrue();
    }

    @Test
    void testDeleteAllByUser() {
        Long userId = 1L;

        boolean deleted = filmLikesRepository.deleteAllByUser(userId);

        assertThat(deleted).isTrue();
    }

}