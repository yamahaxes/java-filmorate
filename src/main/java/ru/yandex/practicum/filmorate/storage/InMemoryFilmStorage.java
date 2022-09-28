package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements Storage<Film> {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int uniqueId = 0;

    @Override
    public Film add(Film film) {
        film.setId(getUniqueId());
        int id = film.getId();
        films.put(id, film);

        log.info("Film created with id={}", id);
        return film;
    }

    @Override
    public Film remove(Film film) {
        int id = film.getId();
        if (!films.containsKey(id)){
            throw new EntityNotFoundException("Film with id=" + id + " not found");
        }

        log.info("Film with id={} removed.", film.getId());
        return films.remove(id);
    }

    @Override
    public Film update(Film film) {
        int id = film.getId();
        if (!films.containsKey(id)){
            throw new EntityNotFoundException("Film with id=" + id + " not found");
        }
        films.put(id, film);

        log.info("Film with id={} updated.", id);
        return film;
    }

    @Override
    public Film get(int id) {
        if (!films.containsKey(id)){
            throw new EntityNotFoundException("Film with id=" + id + " not found");
        }
        log.info("Get film id={}.", id);
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        log.info("Get all films.");
        return new ArrayList<>(films.values());
    }

    private int getUniqueId() {
        return ++uniqueId;
    }
}
