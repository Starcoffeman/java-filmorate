package ru.yandex.practicum.filmorate;



import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	public void testValidUser() {
		User user = new User("user@mail.com", "valid_login", "Valid User", LocalDate.of(1990, 1, 1));
		// Проверяем, что создание объекта User с правильными параметрами не вызывает исключение
	}

	@Test
	public void testUserWithEmptyEmail() {
		assertThrows(ValidationException.class, () -> {
			User user = new User("", "valid_login", "Valid User", LocalDate.of(1990, 1, 1));
		});
	}

	@Test
	public void testUserWithInvalidEmail() {
		assertThrows(ValidationException.class, () -> {
			User user = new User("invalid_email", "valid_login", "Valid User", LocalDate.of(1990, 1, 1));
		});
	}

	@Test
	public void testUserWithEmptyLogin() {
		assertThrows(ValidationException.class, () -> {
			User user = new User("user@mail.com", "", "Valid User", LocalDate.of(1990, 1, 1));
		});
	}

	@Test
	public void testUserWithFutureBirthday() {
		assertThrows(ValidationException.class, () -> {
			User user = new User("user@mail.com", "valid_login", "Valid User", LocalDate.now().plusDays(1));
		});
	}

	@Test
	public void testValidFilm() {
		Film film = new Film("Movie Title", "Description of the movie that is not too long", LocalDate.of(2000, 1, 1), 120);
		// Проверяем, что создание объекта Film с правильными параметрами не вызывает исключение
	}

	@Test
	public void testFilmWithEmptyTitle() {
		assertThrows(ValidationException.class, () -> {
			Film film = new Film("", "Description", LocalDate.of(2000, 1, 1), 120);
		});
	}

	@Test
	public void testFilmWithLongDescription() {
		String longDescription = "This is a very long description that exceeds the maximum allowed length. This description is longer than 200 characters. This description is longer than 200 characters.";
		assertThrows(ValidationException.class, () -> {
			Film film = new Film("Movie Title", longDescription, LocalDate.of(2000, 1, 1), 120);
		});
	}

	@Test
	public void testFilmWithInvalidReleaseDate() {
		assertThrows(ValidationException.class, () -> {
			Film film = new Film("Movie Title", "Description", LocalDate.of(1800, 1, 1), 120);
		});
	}

	@Test
	public void testFilmWithNegativeDuration() {
		assertThrows(ValidationException.class, () -> {
			Film film = new Film("Movie Title", "Description", LocalDate.of(2000, 1, 1), -10);
		});
	}

}
