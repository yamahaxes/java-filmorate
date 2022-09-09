package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

public class UserTest {

    private static Validator validator;
    private User user;
    Set<ConstraintViolation<User>> violations;

    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void init(){
        violations = null;

        user = new User();
        user.setId(1);
        user.setName("name");
        user.setLogin("login");
        user.setEmail("email@yandex.ru");
        user.setBirthday(LocalDate.now().minusYears(20));
    }

    @Test
    public void shouldBeCorrectUser(){
        violations = validator.validate(user);
        Assertions.assertEquals(0, violations.size(), "Не проходит валидацию корректный объект.");
    }

    @Test
    public void shouldBeNotEmptyAndNotNullEmail(){
        user.setEmail(null);
        int shouldBeOneViolation = 1;

        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeOneViolation, violations.size(), "Проходит валидацию с null значением.");

        user.setEmail("");
        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeOneViolation, violations.size(), "Проходит валидацию с пустым значением.");

    }

    @Test
    public void shouldBeCorrectEmail(){
        int shouldBeOneViolation = 1;

        user.setEmail("email");
        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeOneViolation, violations.size(), "Проходит валидацию некорретный email");

    }

    @Test
    public void shouldBeNotBlankLogin(){
        int shouldBeOneViolation = 1;
        int shouldBeTwoViolation = 2;

        user.setLogin(null);
        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeOneViolation, violations.size(), "Проходит валидацию логин с null значением.");

        user.setLogin("");
        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeTwoViolation, violations.size(), "Проходит валидацию логин с пустым значением.");
    }

    @Test
    public void shouldBeLoginWithoutWhiteSpaces(){
        int shouldBeOneViolation = 1;

        user.setLogin("login login");
        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeOneViolation, violations.size(), "Проходит валидацию логин с пробелом внутри.");

        user.setLogin("login ");
        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeOneViolation, violations.size(), "Проходит валидацию логин с пробелом в конце.");

    }

    @Test
    public void shouldBePastBirthday(){
        int shouldBeOneViolation = 1;

        user.setBirthday(LocalDate.now().plusDays(1));
        violations = validator.validate(user);
        Assertions.assertEquals(shouldBeOneViolation, violations.size(), "Проходит валидацию день рождение в будущем.");
    }
}
