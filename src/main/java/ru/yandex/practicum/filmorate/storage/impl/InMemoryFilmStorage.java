package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private final HashMap<Integer, Set<Integer>> likes = new HashMap<>();

    private final UserStorage userStorage;

    private int uniqueId = 0;

    @Autowired
    public InMemoryFilmStorage(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Film add(Film film) {
        film.setId(getUniqueId());
        int id = film.getId();
        films.put(id, film);

        likes.put(id, new HashSet<>());

        log.info("Film created with id={}", id);
        return film;
    }

    @Override
    public Film remove(Film film) {
        int id = film.getId();
        if (!films.containsKey(id)){
            throw new EntityNotFoundException("Film with id=" + id + " not found");
        }

        likes.remove(id);

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

    @Override
    public void like(int filmId, int userId) {
        checkFilm(filmId);
        userStorage.get(userId);

        likes.get(filmId).add(userId);

        log.info("Add like for film id={} from user id={}", filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        checkFilm(filmId);
        userStorage.get(userId);

        likes.get(filmId).remove(userId);

        log.info("Remove like for film id={} from user id={}", filmId, userId);
    }

    @Override
    public List<Film> getPopular(int limit) {
        return likes
                .entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().size() - o1.getValue().size())
                .limit(limit)
                .map(entry -> films.get(entry.getKey()))
                .collect(Collectors.toList());
    }

    private void checkFilm(int id){
        if (!films.containsKey(id)){
            throw new EntityNotFoundException("Film with id=" + id + " not found");
        }
    }

    private int getUniqueId() {
        return ++uniqueId;
    }
}
