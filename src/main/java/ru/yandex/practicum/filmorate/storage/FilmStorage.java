package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends Storage<Film>{
    void like(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> getPopular(int limit);
}
