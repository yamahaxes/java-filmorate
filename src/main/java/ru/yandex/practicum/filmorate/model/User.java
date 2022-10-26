package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
public class User {

    private int id;

    @NotBlank
    @Email(message = "Incorrect email.")
    private String email;

    @NotBlank(message = "Login must not be empty")
    @Pattern(regexp = "^\\S+$", message = "Login must not contain spaces")
    private String login;

    private String name;

    @NotNull
    @Past(message = "Date of birth must contain the past date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

}
