package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {
    T add(T t);

    T remove(T t);

    T update(T t);

    T get(int id);

    List<T> getAll();
}
