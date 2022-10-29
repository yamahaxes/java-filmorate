package ru.yandex.practicum.filmorate.storage.imp;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(value = {"/create_data_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    public void testGetFilm(){

        Film film = filmStorage.get(1);
        assertThat(film).isNotNull()
                .hasFieldOrPropertyWithValue("name", "Film1")
                .hasFieldOrPropertyWithValue("description", "film description")
                .hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1985, 1,1))
                .hasFieldOrPropertyWithValue("duration", 120);

        Mpa mpa = film.getMpa();
        assertThat(mpa).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1);

    }

    @Test
    public void testGetAllFilms(){
        List<Film> films = filmStorage.getAll();

        Assertions.assertNotEquals(null, films);
        Assertions.assertFalse(films.size() < 2);

        Assertions.assertEquals(1, films.get(0).getId());
        Assertions.assertEquals(2, films.get(1).getId());
        Assertions.assertEquals("Film1", films.get(0).getName());
        Assertions.assertEquals("Film2", films.get(1).getName());

    }

    @Test
    public void testAddFilm(){
        Film film = makeFilm();

        Film addedFilm = filmStorage.add(film);

        assertThat(addedFilm)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", film.getName())
                .hasFieldOrPropertyWithValue("description", film.getDescription())
                .hasFieldOrPropertyWithValue("duration", film.getDuration())
                .hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertThat(addedFilm.getMpa()).isNotNull().hasFieldOrPropertyWithValue("id", 1);

        Film filmDb = filmStorage.get(addedFilm.getId());

        assertThat(filmDb)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", film.getName())
                .hasFieldOrPropertyWithValue("description", film.getDescription())
                .hasFieldOrPropertyWithValue("duration", film.getDuration())
                .hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());

        assertThat(addedFilm.getMpa()).isNotNull().hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void testRemoveFilm(){

        List<Film> films = filmStorage.getAll();
        int sizeBefore = films.size();
        Film filmRemoved = new Film();
        filmRemoved.setId(3);
        filmStorage.remove(filmRemoved);
        films = filmStorage.getAll();
        Assertions.assertEquals(sizeBefore - 1, films.size());

    }

    @Test
    public void testUpdateFilm(){
        Film addedFilm = filmStorage.add(makeFilm());
        addedFilm.setName("film updated");
        filmStorage.update(addedFilm);
        Film film = filmStorage.get(addedFilm.getId());
        Assertions.assertEquals("film updated", film.getName());
    }

    @Test
    public void testLikeAndRemoveLikeFilm(){

        filmStorage.like(1, 1);
        filmStorage.like(1, 2);
        filmStorage.like(2, 1);

        List<Film> popularFilms = filmStorage.getPopular(2);
        Assertions.assertNotEquals(null, popularFilms);
        Assertions.assertEquals(1, popularFilms.get(0).getId());
        Assertions.assertEquals(2, popularFilms.get(1).getId());

        filmStorage.removeLike(1, 1);
        filmStorage.like(2, 2);

        popularFilms = filmStorage.getPopular(2);
        Assertions.assertNotEquals(null, popularFilms);
        Assertions.assertEquals(2, popularFilms.get(0).getId());
        Assertions.assertEquals(1, popularFilms.get(1).getId());


    }

    private Film makeFilm(){
        Film film = new Film();
        film.setName("film3");
        film.setDescription("film3 description");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1950, 1,10));
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        return film;
    }
}

