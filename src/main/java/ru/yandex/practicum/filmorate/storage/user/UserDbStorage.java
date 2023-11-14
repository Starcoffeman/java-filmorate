package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(User user) {

    }

    @Override
    public User getUserById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        String sql = "SELECT * FROM USERSALL WHERE ID = ?";

        return (User) jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
    }


    private User makeUser(ResultSet resultSet) throws SQLException {
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");

        LocalDate creationDate = resultSet.getDate("creation_date").toLocalDate();
        return new User(email,login,name,creationDate);

    }

    @Override
    public void removeUser(Integer id) throws UserNotFoundException {

    }

    @Override
    public void updateUser(User updateUser) throws UserNotFoundException {

    }

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public void removeFriendById(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {

    }
}
