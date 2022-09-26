package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int uniqueId = 0;

    @Override
    public Film add(Film film) {
        film.setId(getUniqueId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film remove(Film film) {
        int id = film.getId();
        return films.remove(id);
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }
        return null;
    }

    @Override
    public Film get(int id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    private int getUniqueId() {
        return ++uniqueId;
    }
}
