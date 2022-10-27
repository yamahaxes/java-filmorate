package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa get(int id) {

        String sql = "SELECT * FROM MPA WHERE MPA_ID=?";

        List<Mpa> mpaList = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), id);
        if (mpaList.size() == 0){
            throw new EntityNotFoundException("Mpa id=" + id + " not found");
        }

        log.info("Get mpa id={}", id);

        return mpaList.get(0);
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM MPA";

        List<Mpa> mpaList = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));

        log.info("Get all mpa");

        return mpaList;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(rs.getString("mpa_name"));

        return mpa;
    }
}
