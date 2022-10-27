package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface ReadStorage<T> {
    T get(int id);

    List<T> getAll();
}
