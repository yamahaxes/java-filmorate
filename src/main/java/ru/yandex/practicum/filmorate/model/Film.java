package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.After;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Name must not be empty.")
    private String name;

    @Size(max = 200, message = "Description cannot be longer than 200 characters")
    private String description;

    @NotNull
    @After(year = 1895, month = 12, day = 28, message = "The date must not be earlier than 12/28/1895")
    private Date releaseDate;

    @Positive(message = "The duration of the movie must be positive.")
    private int duration;

    private List<Genre> genres = new ArrayList<>();

    @NotNull
    private Mpa mpa;

}
