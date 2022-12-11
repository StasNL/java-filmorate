package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceTest {

    private final FilmService filmService;

    private final UserService userService;

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void addFilmTest() {
        Film film = createFilm();
        Film filmFromDb = filmService.addFilm(film).get();
        film.setId(filmFromDb.getId());

        assertEquals(film, filmFromDb);
    }

    @Test
    public void updateFilmTest() {
        Film film = createFilm();
        film = filmService.addFilm(film).get();
        Film filmForUpdate = createFilm();
        filmForUpdate.setId(film.getId());
        filmForUpdate.setDescription("Film to update");
        Film filmFromDb = filmService.updateFilm(filmForUpdate).get();

        assertEquals(filmForUpdate,filmFromDb);
    }

    @Test
    public void findFilmTest() {
        Film film = createFilm();
        film = filmService.addFilm(film).get();

        Film filmFromDb = filmService.findFilm(film.getId()).get();

        assertEquals(film, filmFromDb);
    }

    @Test
    public void addLikeTest() {
        Film film = createFilm();
        film = filmService.addFilm(film).get();

        User user = createUser();
        user = userService.addUser(user).get();

        filmService.addLike(film.getId(), user.getId());

        film = filmService.findFilm(film.getId()).get();

        long userId = List.copyOf(film.getLikes()).get(0);

        assertEquals(user.getId(), userId);
    }

    @Test
    public void removeLike() {
        Film film = createFilm();
        film = filmService.addFilm(film).get();

        User user = createUser();
        user = userService.addUser(user).get();

        filmService.addLike(film.getId(), user.getId());
        filmService.removeLike(film.getId(), user.getId());

        assertEquals(0, film.getLikes().size());
    }

    @Test
    public void getPopularFilmTest() {
//        Удаляем все лайки.
        String sql = "DELETE FROM LIKES";
        jdbcTemplate.update(sql);
//        Создаём 2 фильма
        Film film = createFilm();
        Film film1 = filmService.addFilm(film).get();
        film.setName("Film2");
        Film film2 = filmService.addFilm(film).get();

        long film1Id = film1.getId();
        long film2Id = film2.getId();
//     Создаём пользователя
        User user = createUser();
        userService.addUser(user);
        long userId = user.getId();
//     Ставим лайк фильму film2
        filmService.addLike(film2Id, userId);
        film2 = filmService.findFilm(film2Id).get();
//     Проверяем сортировку и получение только одного фильма с id = 2 и лайком.
        List<Film> films = filmService.getPopularFilms(3);

        assertEquals(film2, films.get(0));
    }

    private Film createFilm() {
        return Film.builder()
                .description("Film for test")
                .name("Film")
                .duration(120)
                .likes(new HashSet<>())
                .genres(new HashSet<>())
                .releaseDate(LocalDate.of(1985, 12, 1))
                .mpa(new Mpa(1, "G"))
                .build();
    }

    private static User createUser() {
        return User.builder()
                .name("User")
                .email("user@user.ru")
                .birthday(LocalDate.of(1985, 12, 1))
                .login("user")
                .build();
    }
}
