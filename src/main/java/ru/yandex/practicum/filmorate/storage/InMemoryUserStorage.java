package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements Storage<User> {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int uniqueId = 0;

    @Override
    public User add(User user) {
        user.setId(getUniqueId());
        int id = user.getId();
        users.put(id, user);

        log.info("User created with id={}", id);
        return user;
    }

    @Override
    public User remove(User user) {
        int id = user.getId();
        if (!users.containsKey(id)){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }

        log.info("User with id={} removed", id);
        return users.remove(id);
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        if (!users.containsKey(id)){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
        users.put(id, user);

        log.info("User with id={} updated.", id);
        return user;
    }

    @Override
    public User get(int id) {
        if (!users.containsKey(id)){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
        log.info("Get user with id={}", id);
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("Get all users.");
        return new ArrayList<>(users.values());
    }

    private int getUniqueId() {
        return ++uniqueId;
    }
}
