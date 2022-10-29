package ru.yandex.practicum.filmorate.storage.imp;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(value = {"/create_data_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testGetUser(){

        User user = userStorage.get(1);
        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Evgen")
                .hasFieldOrPropertyWithValue("login", "evgen")
                .hasFieldOrPropertyWithValue("email", "mail@mail.ru")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1988, 1, 14));
    }

    @Test
    public void testGetAllUser(){

        List<User> users = userStorage.getAll();

        assertThat(users).isNotNull();
        Assertions.assertEquals("Evgen", users.get(0).getName());
        Assertions.assertEquals("Olga", users.get(1).getName());

    }

    @Test
    public void testAddUser(){

        User user = makeUser();

        User addedUser = userStorage.add(user);

        assertThat(addedUser)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("login", user.getLogin())
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("birthday", user.getBirthday());

        User userDB = userStorage.get(addedUser.getId());

        assertThat(userDB)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("login", user.getLogin())
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("birthday", user.getBirthday());

    }

    @Test
    public void testRemoveUser(){

        List<User> users = userStorage.getAll();
        assertThat(users).isNotNull();
        int sizeBefore = users.size();
        User userRemoved = new User();
        userRemoved.setId(3);
        userStorage.remove(userRemoved);

        users = userStorage.getAll();
        Assertions.assertEquals(sizeBefore - 1, users.size());
    }


    @Test
    public void testUpdateUser(){

        User user = makeUser();
        User addedUser = userStorage.add(user);

        addedUser.setName("new name");
        addedUser.setLogin("newlogin");
        addedUser.setEmail("newemail@mail.com");
        addedUser.setBirthday(LocalDate.of(1900, 1, 1));

        User updatedUser = userStorage.update(addedUser);
        User userDB = userStorage.get(updatedUser.getId());
        assertThat(userDB)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", addedUser.getName())
                .hasFieldOrPropertyWithValue("login", addedUser.getLogin())
                .hasFieldOrPropertyWithValue("email", addedUser.getEmail())
                .hasFieldOrPropertyWithValue("birthday", addedUser.getBirthday());

    }

    @Test
    public void addUserFriend(){

        List<User> friends = userStorage.getFriends(1);
        userStorage.addFriend(1,2);
        List<User> newFriends = userStorage.getFriends(1);

        Assertions.assertNotEquals(null, friends);
        Assertions.assertNotEquals(null, newFriends);

        Assertions.assertEquals(friends.size() + 1, newFriends.size());

    }

    @Test
    public void removeUserFriend(){

        userStorage.addFriend(1,2);
        List<User> friends = userStorage.getFriends(1);
        userStorage.removeFriend(1, 2);
        List<User> friendsNew = userStorage.getFriends(1);

        Assertions.assertEquals(friends.size() - 1, friendsNew.size());

    }

    @Test
    public void testGetUserFriends(){

        userStorage.addFriend(1, 2);
        List<User> friends = userStorage.getFriends(1);
        Assertions.assertTrue(friends.size() > 0);

    }

    @Test
    public void getCommonUserFriends(){

        User user = userStorage.add(makeUser());
        List<User> commonFriends = userStorage.getCommonFriends(1,2);
        assertThat(commonFriends)
                .isNotNull().asList().hasSize(0);

        userStorage.addFriend(1,user.getId());
        userStorage.addFriend(2,user.getId());

        commonFriends = userStorage.getCommonFriends(1,2);
        assertThat(commonFriends)
                .isNotNull().asList().hasSize(1);


    }

    private User makeUser(){
        User user = new User();
        user.setName("User");
        user.setLogin("user");
        user.setEmail("usermail@mail.com");
        user.setBirthday(LocalDate.of(2000, 1,1));

        return user;
    }
}
