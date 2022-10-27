package ru.yandex.practicum.filmorate.storage;

public interface Storage<T> extends ReadStorage<T>{
    T add(T t);

    T remove(T t);

    T update(T t);
}
