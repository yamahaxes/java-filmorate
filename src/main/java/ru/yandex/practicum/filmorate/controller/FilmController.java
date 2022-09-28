package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms(){
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film){
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film){
        return filmService.updateFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id){
        return filmService.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void like(@PathVariable int id,
                     @PathVariable int userId){
        filmService.like(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable int id,
                           @PathVariable int userId){
        filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count){
        return filmService.getPopular(count);
    }
}
