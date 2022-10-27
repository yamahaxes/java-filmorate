package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre get(int id) {
        String sql = """
                SELECT *
                FROM GENRES
                WHERE GENRE_ID=?
                """;

        List<Genre> genreList = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        if (genreList.size() == 0){
            throw new EntityNotFoundException("Genre id=" + id + " not found");
        }

        log.info("Get genre id={}", id);

        return genreList.get(0);
    }

    @Override
    public List<Genre> getAll() {
        String sql = """
                SELECT *
                FROM GENRES
                """;

        List<Genre> genreList = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));

        log.info("Get genres");

        return genreList;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));

        return genre;
    }

}
