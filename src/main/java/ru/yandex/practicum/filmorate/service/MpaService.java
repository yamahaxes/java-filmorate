package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage storage;

    @Autowired
    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    public Mpa get(int id){
        return storage.get(id);
    }

    public List<Mpa> getAll(){
        return storage.getAll();
    }
}
