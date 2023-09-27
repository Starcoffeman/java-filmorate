package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class FilmorateApplicationTests {

	@Test
	public void testValidFilm() {
		int id = 1;
		String name = "Фильм";
		String description = "Описание фильма";
		LocalDate releaseDate = LocalDate.of(2022, 1, 1);
		int duration = 120;

		Film film = new Film(id, name, description, releaseDate, duration);

		assertNotNull(film);
	}

	@Test
	public void testFilmEmptyName() {
		int id = 1;
		String name = null;
		String description = "Описание фильма";
		LocalDate releaseDate = LocalDate.of(2022, 1, 1);
		int duration = 120;

		assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
	}

	@Test
	public void testFilmDescriptionTooLong() {
		int id = 1;
		String name = "Фильм";
		String description = "Очень длинное описание фильма, которое превышает 200 символов, чтобы вызвать ошибку валидацииaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		LocalDate releaseDate = LocalDate.of(2022, 1, 1);
		int duration = 120;

		assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
	}

	@Test
	public void testFilmReleaseDateInPast() {
		int id = 1;
		String name = "Фильм";
		String description = "Описание фильма";
		LocalDate releaseDate = LocalDate.of(1895, 12, 27); // 27 декабря 1895 года
		int duration = 120;

		assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
	}

	@Test
	public void testNegativeDuration() {
		int id = 1;
		String name = "Фильм";
		String description = "Описание фильма";
		LocalDate releaseDate = LocalDate.of(2022, 1, 1);
		int duration = -10;

		assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
	}

	@Test
	public void testValidUser() {
		int id = 1;
		String email = "user@example.com";
		String login = "username";
		String name = "User Name";
		LocalDate birthday = LocalDate.of(1990, 1, 1);

		User user = new User(id, email, login, name, birthday);

		assertNotNull(user);
	}

	@Test
	public void testInvalidEmail() {
		int id = 1;
		String email = "invalid-email";
		String login = "username";
		String name = "User Name";
		LocalDate birthday = LocalDate.of(1990, 1, 1);

		assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
	}

	@Test
	public void testEmptyLogin() {
		// Empty login
		int id = 1;
		String email = "user@example.com";
		String login = null;
		String name = "User Name";
		LocalDate birthday = LocalDate.of(1990, 1, 1);

		assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
	}

	@Test
	public void testLoginWithSpace() {
		int id = 1;
		String email = "user@example.com";
		String login = "user nameввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввввв";
		String name = "User Name";
		LocalDate birthday = LocalDate.of(1990, 1, 1);

		assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
	}

	@Test
	public void testEmptyName() {
		int id = 1;
		String email = "user@example.com";
		String login = "username";
		String name = "username";
		LocalDate birthday = LocalDate.of(1990, 1, 1);

		User user = new User(id, email, login, name, birthday);

		assertEquals(login, user.getName());
	}

	@Test
	public void testFutureBirthday() {
		int id = 1;
		String email = "user@example.com";
		String login = "username";
		String name = "User Name";
		LocalDate birthday = LocalDate.now().plusDays(1); // Tomorrow

		assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
	}



}