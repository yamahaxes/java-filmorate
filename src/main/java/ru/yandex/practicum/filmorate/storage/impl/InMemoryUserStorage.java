package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private final HashMap<Integer, Set<Integer>> friends = new HashMap<>();

    private int uniqueId = 0;

    @Override
    public User add(User user) {
        user.setId(getUniqueId());
        int id = user.getId();
        users.put(id, user);

        friends.put(id, new HashSet<>());

        log.info("User created with id={}", id);
        return user;
    }

    @Override
    public User remove(User user) {
        int id = user.getId();
        checkUser(id);

        friends.remove(id);

        log.info("User with id={} removed", id);
        return users.remove(id);
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        checkUser(id);

        users.put(id, user);

        log.info("User with id={} updated.", id);
        return user;
    }

    @Override
    public User get(int id) {
        checkUser(id);

        log.info("Get user with id={}", id);
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("Get all users.");
        return new ArrayList<>(users.values());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        checkUser(userId);
        checkUser(friendId);

        friends.get(userId).add(friendId);

        log.info("User with id={} add other user with id={} to friends.", userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        checkUser(userId);
        checkUser(friendId);

        friends.get(userId).remove(friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        checkUser(userId);
        return friends.get(userId)
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        checkUser(userId);
        checkUser(otherUserId);

        Set<Integer> friendsOtherUser = friends.get(otherUserId);
        return friends.get(userId)
                .stream()
                .filter(friendsOtherUser::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }

    private void checkUser(int id){
        if (!users.containsKey(id)){
            throw new EntityNotFoundException("User with id=" + id + " not found.");
        }
    }

    private int getUniqueId() {
        return ++uniqueId;
    }
}
