package ru.yandex.practicum.filmorate.storage.imp;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@Sql(value = {"/create_data_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MpaDbStorageTest {

    private final MpaDbStorage mpaStorage;

    @Test
    public void testGetMpa(){
        Mpa mpa =  mpaStorage.get(1);
        assertThat(mpa).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");

        mpa =  mpaStorage.get(2);
        assertThat(mpa).isNotNull()
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", "PG");
    }

    @Test
    public void testGetAll(){

        List<Mpa> mpaList = mpaStorage.getAll();
        assertThat(mpaList).isNotNull()
                .asList().hasSize(5);

        assertThat(mpaList.get(0)).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "G");

        assertThat(mpaList.get(4)).isNotNull()
                .hasFieldOrPropertyWithValue("id", 5)
                .hasFieldOrPropertyWithValue("name", "NC-17");

    }

}
