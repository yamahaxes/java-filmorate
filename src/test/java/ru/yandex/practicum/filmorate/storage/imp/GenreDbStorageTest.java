package ru.yandex.practicum.filmorate.storage.imp;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(value = {"/create_data_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GenreDbStorageTest {

    private final GenreDbStorage genreStorage;

    @Test
    public void testGetGenre(){

        Genre genre1 = genreStorage.get(1);
        Genre genre2 = genreStorage.get(6);

        assertThat(genre1).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");

        assertThat(genre2).isNotNull()
                .hasFieldOrPropertyWithValue("id", 6)
                .hasFieldOrPropertyWithValue("name", "Документальный");

    }

    @Test
    public void testGetAll(){

        List<Genre> genreList = genreStorage.getAll();
        assertThat(genreList).isNotNull()
                .asList().hasSize(6);

        assertThat(genreList.get(0)).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Комедия");

        assertThat(genreList.get(1)).isNotNull()
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", "Драма");
    }
}
