package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd", fallbackPatterns = "yyyy-MM-dd")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    public boolean addFriend(int id){
        return friends.add(id);
    }

    public boolean deleteFriend(int friendId){
        return friends.remove(friendId);
    }
}
