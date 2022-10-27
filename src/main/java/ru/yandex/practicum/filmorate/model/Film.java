package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Name must not be empty.")
    private String name;

    @Size(max = 200, message = "Description cannot be longer than 200 characters")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive(message = "The duration of the movie must be positive.")
    private int duration;

    private List<Genre> genres = new ArrayList<>();

    @NotNull
    private Mpa mpa;

}
