package ru.yandex.practicum.filmorate.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import javax.validation.Valid;
import java.util.Collection;

@Service
@RestController
@RequestMapping("/films")
public class FilmService {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        inMemoryFilmStorage.addFilm(film);
        return film;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Film> removeFilm(@PathVariable int id) {
        if (inMemoryFilmStorage.films.get(id) != null) {
            inMemoryFilmStorage.removeFilm(id);
            return ResponseEntity.ok(inMemoryFilmStorage.films.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable int id) throws UserNotFoundException {
        if (inMemoryFilmStorage.films.get(id) != null) {
            return ResponseEntity.ok(inMemoryFilmStorage.films.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.films.values();
    }

    @PutMapping
    public Film updateFilm(@PathVariable @Valid Film updateFilm) throws UserNotFoundException {
        inMemoryFilmStorage.updateFilm(updateFilm);
        return updateFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Integer> putLike(@PathVariable int id, int userid) {
        if (inMemoryFilmStorage.films.get(id) != null) {
            inMemoryFilmStorage.films.get(id).getLikes().add(userid);
            return ResponseEntity.ok(inMemoryFilmStorage.films.get(id).getLikes().get(userid));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable int id, int userid) {
        if (inMemoryFilmStorage.films.get(id) != null) {
            if (inMemoryFilmStorage.films.get(id).getLikes().get(userid) != null) {
                inMemoryFilmStorage.films.get(id).getLikes().remove(userid);
                return ResponseEntity.ok(inMemoryFilmStorage.films.get(id));
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}