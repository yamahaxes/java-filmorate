package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping("/films")
    public ResponseEntity<List<Film>> getFilms(){
        return ResponseEntity
                .ok(new ArrayList<>(films.values()));
    }

    @PostMapping("/films")
    public ResponseEntity<Film> create(@Valid @RequestBody Film film){
        log.info("Film created with id={}", film.getId());

        film.setId(getUniqueId());
        films.put(film.getId(), film);

        return ResponseEntity.ok(film);
    }

    @PutMapping("/films")
    public ResponseEntity<?> update(@Valid @RequestBody Film film){
        if (!films.containsKey(film.getId())){
            log.warn("Film with id={} not found.", film.getId());

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        log.info("Film with id={} updated.", film.getId());

        films.put(film.getId(), film);
        return ResponseEntity.ok(film);
    }

    private int getUniqueId(){
        return ++id;
    }

}
