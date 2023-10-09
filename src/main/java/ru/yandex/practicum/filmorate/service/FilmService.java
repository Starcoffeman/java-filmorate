package ru.yandex.practicum.filmorate.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.HashMap;


@Service
@RestController
@RequestMapping("/films")
public class FilmService {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody @Valid Film film) {
        inMemoryFilmStorage.addFilm(film);
        return ResponseEntity.ok(inMemoryFilmStorage.films.get(film.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<Integer, Film>> removeFilm(@PathVariable Integer id) {
        if (inMemoryFilmStorage.films.get(id) != null) {
            inMemoryFilmStorage.removeFilm(id);
            return ResponseEntity.ok(inMemoryFilmStorage.films);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer id) {
        if (inMemoryFilmStorage.films.get(id) != null) {
            return ResponseEntity.ok(inMemoryFilmStorage.films.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping
    public void updateFilm(@PathVariable Film updateFilm) throws UserNotFoundException {
        inMemoryFilmStorage.updateFilm(updateFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> putLike(@PathVariable Integer id, @PathVariable Integer userid) {
        if (inMemoryFilmStorage.films.get(id) != null & inMemoryFilmStorage.films.get(userid) != null) {
            inMemoryFilmStorage.films.get(id).getLikes().add(userid);
            return ResponseEntity.ok(inMemoryFilmStorage.films.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable Integer id, @PathVariable Integer userid) {
        if (inMemoryFilmStorage.films.get(id) != null & inMemoryFilmStorage.films.get(userid) != null) {
            inMemoryFilmStorage.films.get(id).getLikes().remove(userid);
            inMemoryFilmStorage.films.get(userid).getLikes().remove(id);
            return ResponseEntity.ok(inMemoryFilmStorage.films.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

/*    @GetMapping("/popular?count={count}")
    public ResponseEntity<List<Film>> getMostPopularFilms(@PathVariable Integer count){


    }*/

}
