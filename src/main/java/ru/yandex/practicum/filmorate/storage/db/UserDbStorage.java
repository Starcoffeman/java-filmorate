package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addUser(User user) {

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        String sql = "INSERT INTO USERS (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setTimestamp(4, Timestamp.valueOf(user.getBirthday().atStartOfDay()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void removeUser(Integer id) throws UserNotFoundException, IdIsNegativeException {
        if (id < 0) {
            throw new IdIsNegativeException("User ID cannot be negative.");
        }

        String sql = "DELETE FROM USERS WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
    }

    @Override
    public void updateUser(User updateUser) throws UserNotFoundException, IdIsNegativeException {
        if (updateUser.getId() < 0) {
            throw new IdIsNegativeException("User ID cannot be negative.");
        }

        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, updateUser.getEmail(), updateUser.getLogin(),
                updateUser.getName(), updateUser.getBirthday(), updateUser.getId());
        if (affectedRows == 0) {
            throw new UserNotFoundException("User with ID " + updateUser.getId() + " not found.");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class));
    }

    @Override
    public User getUserById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        if (id < 0) {
            throw new IdIsNegativeException("User ID cannot be negative.");
        }

        String sql = "SELECT * FROM USERS WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(User.class), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User with ID " + id + " not found.");
        }
    }

    @Override
    public void removeFriendById(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        if (id < 0 || otherId < 0) {
            throw new IdIsNegativeException("User ID cannot be negative.");
        }

        getUserById(id);

        String sql = "DELETE FROM FRIENDS WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        int affectedRows = jdbcTemplate.update(sql, id, otherId, otherId, id);

        if (affectedRows == 0) {
            throw new UserNotFoundException("Friendship not found between users with ID " + id + " and " + otherId);
        }
    }

    @Override
    public List<User> getFriendListById(Integer id) {
        String sql = "SELECT u.* FROM USERS u INNER JOIN FRIENDS f ON u.id = f.friend_id WHERE f.user_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class), id);
    }


    @Override
    public User getFriendById(Integer id, Integer friendId) {
        String sql = "SELECT u.* FROM USERS u INNER JOIN FRIENDS f ON u.id = f.friend_id WHERE f.user_id = ? AND f.friend_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(User.class), id, friendId);
        } catch (EmptyResultDataAccessException e) {
            return null; // Друг не найден
        }
    }

    @Override
    public void addFriend(Integer id, Integer friendId) throws IdIsNegativeException {
        if (id < 0 || friendId < 0) {
            throw new IdIsNegativeException("User ID cannot be negative.");
        }

        String sql = "INSERT INTO FRIENDS (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public List<User> getCommonFriendList(Integer id, Integer otherId) {
        String sql = "SELECT u.* FROM USERS u INNER JOIN FRIENDS f1 ON u.id = f1.friend_id " +
                "INNER JOIN FRIENDS f2 ON u.id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(User.class), id, otherId);
    }
}
