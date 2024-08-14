package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Rating;
import java.util.List;
import java.util.Optional;

@Repository
public class RatingRepository extends BaseRepository<Rating> {

    private static final String TABLE_NAME_RATING = "rating";

    public RatingRepository(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    public List<Rating> findAll() {
        return findMany(BaseQueries.getAll(TABLE_NAME_RATING));
    }

    public Optional<Rating> findById(Long id) {
        return findOne(BaseQueries.getById(TABLE_NAME_RATING), id);
    }

    public Rating getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new ValidateException(String.format("Рейтинг с ID: %d не найден", id)));
    }

}