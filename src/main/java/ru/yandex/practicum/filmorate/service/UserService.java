package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final Storage<User> userStorage;

    @Autowired
    public UserService(Storage<User> userStorage){
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User createUser(User user) {
        if (user.getName() == null
                || user.getName().isBlank()){
            user.setName(user.getLogin());
        }

        return userStorage.add(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public User getUser(int id){
        return userStorage.get(id);
    }

    public void addFriend(int id, int friendId) {
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);

        user.addFriend(friendId);
        friend.addFriend(id);

        userStorage.update(user);
        userStorage.update(friend);

        log.info("User with id={} and user with id={} became friends.", id, friendId);
    }

    public void removeFriend(int id, int friendId){
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(id);

        userStorage.update(user);
        userStorage.update(friend);

        log.info("User with id={} and user with id={} are no longer friends.", id, friendId);
    }

    public List<User> getFriends(int id) {
        User user = userStorage.get(id);

        log.info("Get friends id={}.", id);

        return user
                .getFriends()
                .stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        User user = userStorage.get(id);
        User otherUser = userStorage.get(otherId);

        log.info("Get common friends id={} and id={}.", id, otherId);
        return user
                .getFriends()
                .stream()
                .filter(idUser -> otherUser.getFriends().contains(idUser))
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
