package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        String sql = """
                INSERT INTO users (user_name, login, email, birthday)
                    VALUES(?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        log.info("User created to db with id={}", user.getId());

        return user;
    }

    @Override
    public User remove(User user) {
        String sql = """
                DELETE from users WHERE user_id=?
                """;

        jdbcTemplate.update(sql, user.getId());

        log.info("User removed from db with id={}", user.getId());

        return user;
    }

    @Override
    public User update(User user) {
        String sql = """
                UPDATE users SET user_name=?, login=?, email=?, birthday=? WHERE user_id=?
                """;

        jdbcTemplate.update(sql, user.getName(),
                user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());

        log.info("User updated to db with id={}", user.getId());

        return get(user.getId());
    }

    @Override
    public User get(int id) {
        String sql = """
                SELECT * FROM users WHERE user_id=?
                """;

        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        if (users.size() == 0) {
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }

        log.info("User was get from db with id={}", id);

        return users.get(0);
    }

    @Override
    public List<User> getAll() {
        String sql = """
                SELECT * FROM users
                """;

        log.info("Get all users from db");

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public void addFriend(int userId, int friendId) {

        String sql = """
                INSERT INTO friends(user_id, friend_id)
                SELECT ?1, ?2 FROM dual
                WHERE NOT EXISTS(SELECT 1 FROM friends WHERE user_id=?1 AND friend_id=?2);
                """;
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (DataAccessException e){
            throw new EntityNotFoundException("User id=" + userId + " and friend id=" + friendId + " insert error.");
        }
    }

    @Override
    public void removeFriend(int userId, int friendId) {

        String sql = """
                DELETE FROM friends
                WHERE user_id = ? AND friend_id = ?
                """;
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {

        String sql = """
                SELECT u.*
                FROM friends AS fr
                INNER JOIN users as u on fr.friend_id = u.user_id
                WHERE fr.user_id = ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {

        String sql = """
                SELECT
                    u.*
                FROM friends AS uf
                    INNER JOIN friends AS ff
                        ON uf.friend_id = ff.friend_id AND uf.user_id = ? AND ff.user_id = ?
                    INNER JOIN users AS u
                        ON uf.friend_id = u.user_id
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, otherUserId);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setName(rs.getString("user_name"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }
}
