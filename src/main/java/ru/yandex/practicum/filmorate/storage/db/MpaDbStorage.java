package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sql = "select * from MPA";
        return jdbcTemplate.query(sql, MpaDbStorage::createMpa);
    }

    @Override
    public Mpa getById(int id) throws MpaNotFoundException {
        String sql = "select * from MPA where id=?";
        List<Mpa> mpas = jdbcTemplate.query(sql, MpaDbStorage::createMpa,id);

        if(!mpas.isEmpty()){
            return mpas.get(0);
        } else {
            throw new MpaNotFoundException("Mpa not found");
        }


    }

    static Mpa createMpa(ResultSet rs, int rowNuw) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();

    }
}
