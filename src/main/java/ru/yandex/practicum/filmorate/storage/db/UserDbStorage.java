package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
import java.util.Collection;
import java.util.List;

@Component
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> getAllUsers() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }



    @Override
    public void addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

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
        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                updateUser.getEmail(),
                updateUser.getLogin(),
                updateUser.getName(),
                updateUser.getBirthday(),
                updateUser.getId());

        if (rowsAffected == 0) {
            throw new UserNotFoundException("User with ID " + updateUser.getId() + " not found.");
        }
    }



    @Override
    public User getUserById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        if (id < 0) {
            throw new IdIsNegativeException("User ID cannot be negative.");
        }

        String sql = "SELECT * FROM USERS WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
    }



    @Override
    public void removeFriendById(Integer id, Integer otherId) throws IdIsNegativeException, UserNotFoundException {
        if (id < 1 || otherId < 1) {
            throw new IdIsNegativeException("User IDs cannot be negative.");
        }

        String sql = "DELETE FROM FRIENDS WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        int rowsAffected = jdbcTemplate.update(sql, id, otherId, otherId, id);

        if (rowsAffected == 0) {
            throw new UserNotFoundException("Friendship not found between users with ID " + id + " and " + otherId + ".");
        }
    }

    @Override
    public List<User> getCommonFriendList(Integer id, Integer otherId) {
        String sql = "SELECT u.* FROM USERS u " +
                "JOIN FRIENDS f1 ON u.id = f1.friend_id " +
                "JOIN FRIENDS f2 ON u.id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id, otherId);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) throws IdIsNegativeException {
        if (id<1 ||friendId<1){
            throw new IdIsNegativeException("Id is negative");
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FRIENDS");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("friend_id", friendId);

        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public List<User> getFriendListById(Integer id) {
        String sql = "SELECT u.* FROM USERS u " +
                "JOIN FRIENDS f ON u.id = f.friend_id " +
                "WHERE f.user_id = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), id);
    }
}
