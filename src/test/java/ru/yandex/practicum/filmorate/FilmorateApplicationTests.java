package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.sql.Date;


@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class FilmorateApplicationTests {

	private final UserDbStorage userStorage;

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetUserById() {
		// Создание и получение пользователя
		User user = new User();
		user.setName("Evgen");
		user.setLogin("evgen");
		user.setEmail("mail@yandex.ru");
		user.setBirthday(Date.valueOf("1988-01-01").toLocalDate());

		User testUser = userStorage.add(user);

		Assertions.assertNotEquals(testUser, null);
		Assertions.assertEquals(testUser.getId(), 1);
		Assertions.assertEquals(testUser.getName(), "Evgen");
		Assertions.assertEquals(testUser.getLogin(), "evgen");
		Assertions.assertEquals(testUser.getEmail(), "mail@yandex.ru");
		Assertions.assertEquals(testUser.getBirthday(), Date.valueOf("1988-01-01").toLocalDate());
	}

}
