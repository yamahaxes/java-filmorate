package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    public List<Film> getFilms(){
        return filmStorage.getAll();
    }

    public Film createFilm(Film film){
        if (!film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28))){
            throw new ValidationException("Release date must be after or equal 28-12-1895");
        }
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
       filmStorage.like(id, userId);
    }

    public void removeLike(int id, int userId){
        filmStorage.removeLike(id, userId);
    }

    public List<Film> getPopular(int limit) {
        return filmStorage.getPopular(limit);
    }
}
