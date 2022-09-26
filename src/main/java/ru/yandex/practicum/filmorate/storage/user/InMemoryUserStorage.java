package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final HashMap<Integer, User> users = new HashMap<>();
    private int uniqueId = 0;

    @Override
    public User add(User user) {
        user.setId(getUniqueId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User remove(User user) {
        return users.remove(user.getId());
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
            return user;
        }
        return null;
    }

    @Override
    public User get(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private int getUniqueId() {
        return ++uniqueId;
    }
}
