package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.BaseIntegrationTest;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RatingRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Test
    void testFindAll() {
        List<Rating> ratings = ratingRepository.findAll();

        assertThat(ratings)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(rating -> assertThat(rating).hasNoNullFieldsOrProperties());
    }

    @Test
    void testFindById_RatingExists() {
        Optional<Rating> ratingOptional = ratingRepository.findById(1L);

        assertThat(ratingOptional)
            .isPresent()
            .hasValueSatisfying(rating -> {
                assertThat(rating).hasFieldOrPropertyWithValue("id", 1L);
                assertThat(rating.getName()).isEqualTo("G");
            });
    }

    @Test
    void testFindById_RatingDoesNotExist() {
        Optional<Rating> ratingOptional = ratingRepository.findById(999L);

        assertThat(ratingOptional).isNotPresent();
    }

    @Test
    void testGetById_RatingExists() {
        Rating rating = ratingRepository.getById(1L);

        assertThat(rating)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    void testGetById_RatingDoesNotExist() {
        assertThatThrownBy(() -> ratingRepository.getById(999L))
            .isInstanceOf(ValidateException.class)
            .hasMessageContaining("Рейтинг с ID: 999 не найден");
    }

}