package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
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
       filmStorage.like(id, userId);
    }

    public void removeLike(int id, int userId){
        filmStorage.removeLike(id, userId);
    }

    public List<Film> getPopular(int limit) {
        return filmStorage.getPopular(limit);
    }
}
