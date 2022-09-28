package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;

    @Autowired
    public FilmService(Storage<Film> filmStorage,
                       Storage<User> userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms(){
        return filmStorage.getAll();
    }

    public Film createFilm(Film film){
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film){
        return filmStorage.update(film);
    }

    public Film removeFilm(Film film){
        return filmStorage.remove(film);
    }

    public Film getFilm(int id){
        return filmStorage.get(id);
    }

    public void like(int id, int userId) {
        Film film = filmStorage.get(id);
        User user = userStorage.get(userId);

        film.addLike(user.getId());
        filmStorage.update(film);

        log.info("User with id={} has liked film with id={}", userId, id);
    }

    public void removeLike(int id, int userId){
        Film film = filmStorage.get(id);
        User user = userStorage.get(userId);

        if (!film.getLikes()
                .contains(userId)){
            throw new EntityNotFoundException("Film with id=" + id + " did not like the user with id=" + userId);
        }
        film.removeLike(user.getId());
        filmStorage.update(film);

        log.info("User with id={} has removed like from film with id={}", userId, id);
    }

    public List<Film> getPopular(int count) {
        return filmStorage
                .getAll()
                .stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
