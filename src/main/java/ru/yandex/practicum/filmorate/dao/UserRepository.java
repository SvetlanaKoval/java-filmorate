package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepository extends BaseRepository<User> {

    private static final String TABLE_NAME_USERS = "users";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(BaseQueries.getAll(TABLE_NAME_USERS));
    }

    private Optional<User> findById(Long id) {
        return findOne(BaseQueries.getById(TABLE_NAME_USERS), id);
    }

    public User getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("Пользователь  с ID: %d не найден", id)));
    }

    public List<User> findAllByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return findMany(BaseQueries.getAllById(TABLE_NAME_USERS, ids.size()), ids.toArray());
    }

    public Optional<User> findByEmail(String email) {
        return findOne(BaseQueries.getByColum(TABLE_NAME_USERS, "email"), email);
    }

    public User save(User user) {
        Long id = insert(
            BaseQueries.insert(TABLE_NAME_USERS, "email", "login", "name", "birthday"),
            user.getEmail(),
            user.getLogin(),
            user.getName(),
            Date.valueOf(user.getBirthday())
        );

        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(BaseQueries.update(TABLE_NAME_USERS, "email", "login", "name", "birthday"),
            user.getEmail(),
            user.getLogin(),
            user.getName(),
            Date.valueOf(user.getBirthday()),
            user.getId()
        );

        return user;
    }

    public boolean delete(Long id) {
        return delete(BaseQueries.delete(TABLE_NAME_USERS), id);
    }

}