package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms(){
        log.info("Get all films.");
        return filmStorage.getAll();
    }

    public Film createFilm(Film film){
        Film createdFilm = filmStorage.add(film);
        log.info("Film created with id={}", createdFilm.getId());
        return createdFilm;
    }

    public Film updateFilm(Film film){
        Film updatedFilm = filmStorage.update(film);
        if (updatedFilm == null){
            log.warn("Film with id={} not found.", film.getId());
            throw new FilmNotFoundException("Film with id=" + film.getId() + " not found");
        } else {
            log.info("Film with id={} updated.", film.getId());
        }

        return updatedFilm;
    }

    public Film removeFilm(Film film){
        Film removedFilm = filmStorage.remove(film);
        if (removedFilm == null){
            log.warn("Film with id={} not found.", film.getId());
            throw new FilmNotFoundException("Film with id=" + film.getId() + " not found");
        } else {
            log.info("Film with id={} removed.", film.getId());
        }

        return removedFilm;
    }

    public Film getFilm(int id){
        Film film = filmStorage.get(id);
        if (film == null){
            log.warn("Film with id={} not found.", id);
            throw new FilmNotFoundException("Film with id=" + id + " not found");
        } else{
            log.info("Get film id={}.", id);
        }

        return film;
    }

    public void like(int id, int userId) {
        Film film = getFilm(id);
        User user = userStorage.get(userId); // check user id

        film.addLike(user.getId());
        filmStorage.update(film);

        log.info("User with id={} has liked film with id={}", userId, id);
    }

    public void removeLike(int id, int userId){
        Film film = getFilm(id);
        User user = userStorage.get(userId);

        if (!film.getLikes().contains(userId)){
            log.warn("Like not found (filmId={}, userId={})", id, userId);
            throw new LikeNotFoundException("Film with id=" + id + " did not like the user with id=" + userId);
        }
        film.removeLike(user.getId());
        filmStorage.update(film);

        log.info("User with id={} has removed like from film with id={}", user, id);
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
