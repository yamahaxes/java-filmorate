package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public Film add(Film film) {
        String sql ="""
                INSERT INTO films(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
                    VALUES ( ?1, ?2, ?3 , ?4, ?5 )
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());

            return ps;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        StringBuilder sqlBuilder = getGenresInsert(film);
        if (!sqlBuilder.isEmpty()){
            jdbcTemplate.update(sqlBuilder.toString());
        }

        log.info("Add film id={}", film.getId());

        return get(film.getId());
    }

    @Override
    public Film remove(Film film) {
        String sql = """
                DELETE FROM FILMS_GENRES WHERE FILM_ID = ?1;
                DELETE FROM FILMS WHERE FILM_ID = ?1;
                """;

        jdbcTemplate.update(sql, film.getId());

        log.info("Remove film id={}", film.getId());

        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = """
                UPDATE FILMS SET FILM_NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, MPA_ID=?
                WHERE FILM_ID=?
                """;

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                film.getId());


        jdbcTemplate.update("DELETE FROM FILMS_GENRES WHERE FILM_ID=?", film.getId());
        StringBuilder sqlBuilder = getGenresInsert(film);

        if (!sqlBuilder.isEmpty()){
            jdbcTemplate.update(sqlBuilder.toString());
        }

        log.info("Update film id={}", film.getId());

        return get(film.getId());
    }

    @Override
    public Film get(int id) {
        String sql  = """
                SELECT
                    F.*,
                    M.MPA_NAME
                FROM FILMS F
                LEFT OUTER JOIN MPA M on M.MPA_ID = F.MPA_ID
                WHERE F.FILM_ID = ?
                """;
        List<Film> filmList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);

        if (filmList.size() == 0){
            throw new EntityNotFoundException("Film with id=" + id + " not found.");
        }

        Film film = filmList.get(0);

        sql = """
        SELECT
            G2.*
        FROM FILMS_GENRES FG
            INNER JOIN GENRES G2
            on G2.GENRE_ID = FG.GENRE_ID AND FG.FILM_ID = ?
        """;

        List<Genre> genreList = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        film.setGenres(new HashSet<>(genreList));

        log.info("Get film id={}", film.getId());

        return film;
    }

    @Override
    public List<Film> getAll() {
        String sql = """
                SELECT
                    F.*,
                    M.MPA_NAME
                FROM FILMS F
                LEFT OUTER JOIN MPA M on M.MPA_ID = F.MPA_ID
                """;

        List<Film> filmList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));

        fillGenres(filmList);

        log.info("Get all films");

        return filmList;
    }

    @Override
    public void like(int filmId, int userId) {
        String sql = """
                INSERT INTO likes (film_id, user_id)
                SELECT ?1, ?2 FROM dual
                WHERE NOT EXISTS(SELECT 1 FROM likes WHERE film_id=?1 AND user_id=?2)
                """;
        try {
            jdbcTemplate.update(sql, filmId, userId);
        } catch (DataAccessException e){
            throw new EntityNotFoundException("Film id=" + filmId + " and user id=" + userId + " insert error.");
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = """
        SELECT *
        FROM LIKES WHERE FILM_ID=? AND USER_ID=?
        """;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, filmId, userId);
        rowSet.last();
        if (rowSet.getRow() == 0){
            throw new EntityNotFoundException("Film id=" + filmId + " and user id=" + userId + " not found.");
        }

        sql = """
                DELETE FROM likes
                WHERE film_id = ? AND user_id = ?
                """;

        log.info("Remove like for film id={} and user id={}", filmId, userId);

        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getPopular(int limit) {
        String sql = """
                SELECT
                    F.*,
                    M.MPA_NAME
                FROM FILMS AS F
                LEFT JOIN LIKES L on F.FILM_ID = L.FILM_ID
                LEFT JOIN MPA M on M.MPA_ID = F.MPA_ID
                GROUP BY F.FILM_ID
                ORDER BY COUNT(DISTINCT L.USER_ID) DESC
                LIMIT ?
                """;
        List<Film> filmList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), limit);
        fillGenres(filmList);

        log.info("Get popular. Limit={}", limit);

        return filmList;
    }

    private void fillGenres(List<Film> filmList){
        String sql = """
        SELECT
            G2.*
        FROM FILMS_GENRES FG
            INNER JOIN GENRES G2
            on G2.GENRE_ID = FG.GENRE_ID AND FG.FILM_ID = ?
        """;

        for (Film film: filmList) {
            List<Genre> genreList = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), film.getId());
            film.setGenres(new HashSet<>(genreList));
        }
    }

    private StringBuilder getGenresInsert(Film film){
        StringBuilder sqlBuilder = new StringBuilder();
        for (Genre genre: film.getGenres()) {
            sqlBuilder.append(String
                    .format("INSERT INTO films_genres(film_id, genre_id) VALUES ('%s', '%s');",
                            film.getId(), genre.getId()));
        }

        return sqlBuilder;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("film_id"));
        film.setName(rs.getString("film_name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(makeMpa(rs));

        return film;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("mpa_name"));
        return mpa;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));
        return genre;
    }
}
