package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage){
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

        User createdUser = userStorage.add(user);
        log.info("User created with id={}", createdUser.getId());

        return createdUser;
    }

    public User updateUser(User user) {

        User updatedUser = userStorage.update(user);

        if (updatedUser == null){
            log.warn("User with id={} not found.", user.getId());
            throw new UserNotFoundException("User with id=" + user.getId() + " not found.");
        }

        log.info("User with id={} updated.", user.getId());
        return updatedUser;
    }

    public User getUser(int id){
        User user = userStorage.get(id);
        if (user == null){
            log.warn("User with id={} not found.", id);
            throw new UserNotFoundException("User with id=" + id + " not found.");
        } else {
            log.info("Get user with id={}", id);
        }

        return user;
    }

    public void addFriend(int id, int friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);

        user.addFriend(friendId);
        friend.addFriend(id);

        userStorage.update(user);
        userStorage.update(friend);

        log.info("User with id={} and user with id={} became friends.", id, friendId);
    }

    public void removeFriend(int id, int friendId){
        User user = getUser(id);
        User friend = getUser(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(id);

        userStorage.update(user);
        userStorage.update(friend);

        log.info("User with id={} and user with id={} are no longer friends.", id, friendId);
    }

    public List<User> getFriends(int id) {
        User user = getUser(id);
        List<User> friends = new ArrayList<>();
        for (int friendId: user.getFriends()) {
            friends.add(getUser(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        User user = getUser(id);
        User otherUser = getUser(otherId);

        List<User> commonFriends = new ArrayList<>();
        Set<Integer> friendsIdOther = otherUser.getFriends();

        for (int friendId: user.getFriends()) {
            if (friendsIdOther.contains(friendId)){
                User commonFriend = getUser(friendId);
                commonFriends.add(commonFriend);
            }
        }

        log.info("Get common friends id={} and id={}.", id, otherId);
        return commonFriends;
    }
}
