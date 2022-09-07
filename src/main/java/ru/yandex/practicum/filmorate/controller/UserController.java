package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity
                .ok(new ArrayList<>(users.values()));
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@Valid @RequestBody User user){
        log.info("User created with id={}", user.getId());

        if (user.getName() == null
                || user.getName().isBlank()){
            user.setName(user.getLogin());
        }

        user.setId(getUniqueId());
        users.put(user.getId(), user);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
    public ResponseEntity<User> update(@Valid @RequestBody User user){
        if (!users.containsKey(user.getId())){
            log.warn("User with id={} not found.", user.getId());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        log.info("User with id={} updated.", user.getId());

        users.put(user.getId(), user);
        return ResponseEntity.ok(user);
    }

    private int getUniqueId(){
        return ++id;
    }

}
