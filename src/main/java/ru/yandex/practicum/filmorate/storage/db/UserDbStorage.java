package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAllUsers() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, UserDbStorage::createUser);
    }

    static User createUser(ResultSet rs, int i) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .build();

    }

    @Override
    public void addUser(User user) {
/*        String sql = "merge into USERS (id) values (?)";
        jdbcTemplate.update(sql, user.getId());*/
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);
        user.setId(generatedId.intValue());
    }

    @Override
    public void removeUser(Integer id) throws UserNotFoundException {
        String sql = "delete from USERS where id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
    }

    @Override
    public void updateUser(User updateUser) throws UserNotFoundException {
        String sql = "update USERS set id = ? where id = ?";
        int rowsAffected = jdbcTemplate.update(sql, updateUser.getId(), updateUser.getId());
        if (rowsAffected == 0) {
            throw new UserNotFoundException("User with ID " + updateUser.getId() + " not found.");
        }
    }



    @Override
    public User getUserById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        if (id < 0) {
            throw new IdIsNegativeException("User ID cannot be negative.");
        }

        String sql = "select * from USERS where id = ?";
        return jdbcTemplate.queryForObject(sql, UserDbStorage::createUser, id);
    }

    @Override
    public void removeFriendById(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {

    }
}
