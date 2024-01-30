package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFilm_ValidFilm_ReturnsCreatedFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("This is a test film");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);

        Film createdFilm = new Film();
        createdFilm.setId(1L);
        createdFilm.setName("Test Film");
        createdFilm.setDescription("This is a test film");
        createdFilm.setReleaseDate(LocalDate.now());
        createdFilm.setDuration(120);
        FilmController filmController = new FilmController(filmService);

        when(filmService.create(film)).thenReturn(createdFilm);

        Film result = filmController.add(film);

        assertEquals(createdFilm, result);
        verify(filmService, times(1)).create(film);
    }

    @Test
    void updateFilm_ValidFilm_ReturnsUpdatedFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Updated Film");
        film.setDescription("This is an updated film");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(150);

        Film updatedFilm = new Film();
        updatedFilm.setId(1L);
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("This is an updated film");
        updatedFilm.setReleaseDate(LocalDate.now());
        updatedFilm.setDuration(150);

        FilmController filmController;

        filmController = new FilmController(filmService);

        when(filmService.update(film)).thenReturn(updatedFilm);

        Film result = filmController.update(film);

        assertEquals(updatedFilm, result);
        verify(filmService, times(1)).update(film);
    }

    @Test
    public void testGetAllFilms() throws Exception {
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Film 1");

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Film 2");

        List<Film> films = Arrays.asList(film1, film2);

        when(filmService.findAll()).thenReturn(films);

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Film 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Film 2"));

        verify(filmService, times(1)).findAll();
    }

    @Test
    public void testGetFilmById() throws Exception {
        Film film = new Film();
        film.setId(1L);
        film.setName("Film 1");

        when(filmService.findById(1L)).thenReturn(film);

        mockMvc.perform(get("/films/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Film 1"));

        verify(filmService, times(1)).findById(1L);
    }

    @Test
    public void testGetPopularFilms() throws Exception {
        Film film1 = new Film();
        film1.setId(1L);
        film1.setName("Film 1");

        Film film2 = new Film();
        film2.setId(2L);
        film2.setName("Film 2");

        List<Film> films = Arrays.asList(film1, film2);

        when(filmService.findPopular(10)).thenReturn(films);

        mockMvc.perform(get("/films/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Film 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Film 2"));

        verify(filmService, times(1)).findPopular(10);
    }
}