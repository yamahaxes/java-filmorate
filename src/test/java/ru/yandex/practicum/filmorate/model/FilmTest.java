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

public class FilmTest {

    private static Validator validator;
    private Film film;
    Set<ConstraintViolation<Film>> violations;

    @BeforeAll
    public static void setup(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @BeforeEach
    public void init(){
        violations = null;

        film = new Film();
        film.setId(1);
        film.setName("FilmName");
        film.setDescription("description of the film");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.now().minusYears(1));
    }

    @Test
    public void shouldBeCorrectFilm(){
        violations = validator.validate(film);
        Assertions.assertEquals(0, violations.size(), "Не прошел валидацию корректный объект.");
    }

    @Test
    public void shouldBeNotNullOrNotEmptyName(){
        film.setName(null);
        Assertions.assertNotEquals(0, validator.validate(film).size(), "Прошел валидацию с null name");

        film.setName("");
        Assertions.assertNotEquals(0, validator.validate(film).size(), "Прошел валидацию с пустым name");
    }

    @Test
    public void shouldBeMaxLengthDescription200(){
        film.setDescription("a".repeat(201));
        Assertions.assertNotEquals(0, validator.validate(film).size(), "Прошел валидация description 201.");
    }

    @Test
    public void shouldBeBeReleaseDateMore1895(){
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        Assertions.assertEquals(0, validator.validate(film).size(), "Дата релиза(28/12/1895) не проходит валидацию.");

        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Assertions.assertNotEquals(0, validator.validate(film).size(), "Дата релиза(27/12/1895) проходит валидацию.");
    }

    @Test
    public void shouldBeDurationMore0(){
        film.setDuration(0);
        Assertions.assertNotEquals(0, validator.validate(film).size(), "Проходит валидацию протяженность фильма в 0.");

        film.setDuration(-1);
        Assertions.assertNotEquals(0, validator.validate(film).size(), "Проходит валидацию отрицательная протяженность фильма.");

    }
}
