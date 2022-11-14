package ru.yandex.practicum.filmorate.ServiceTests;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.model.utils.Genre.COMEDY;
import static ru.yandex.practicum.filmorate.model.utils.Rating.PG13;

public class FilmServiceTest {

    FilmService filmService;

    @BeforeEach
    void createFilmServiceTest() {
        this.filmService = new FilmService(new InMemoryFilmStorage());
    }

    @Test
    @DisplayName("Тестирование создания фильма.")
    void add() {
        Film filmTest = createFilm("Creation test film");

        filmService.add(filmTest);
        long id = 1;
        filmTest.setId(id);

        assertEquals(1, filmTest.getId());
        assertEquals(1, filmService.getAll().size());
        assertEquals(filmTest, filmService.findFilm(id));
    }

    @Test
    @DisplayName("Тестирование обновления фильма.")
    void update() {
        long id = 1;
        Film film = createFilm("Update test film");

        filmService.add(film);
        film.setId(id);

        Film updateTestFilm = Film.builder()
                .id(id)
                .name("Update Test film")
                .description("Updated film")
                .duration(90)
                .releaseDate(LocalDate.of(1985, 12, 1))
                .genre(COMEDY.getGenre())
                .rating(PG13.getRating())
                .build();

        filmService.update(updateTestFilm);

        assertEquals(1, filmService.getAll().size());
        assertEquals(updateTestFilm, filmService.findFilm(id));
    }

    @Test
    @DisplayName("Тестирование удаления фильма.")
    void remove() {
        long id = 1;

        Film removeTestFilm = filmService.add(createFilm("Remove test film"));
        filmService.remove(removeTestFilm);

        assertEquals(0, filmService.getAll().size());

        Throwable thrown = assertThrows(NotFoundException.class, () -> filmService.findFilm(id));
        String errorMessage = thrown.getMessage();
        assertEquals("Фильм отсутствует в библиотеке.", errorMessage);
    }

    @Test
    @DisplayName("Тестирование поиска фильма.")
    void findFilm() {
        Film createdFilm = filmService.add(createFilm("Find test film"));
        Film foundFilm = filmService.findFilm(1L);

        assertEquals(createdFilm, foundFilm);
    }

    @Test
    @DisplayName("Тестирование получения списка всех фильмов")
    void getAll() {
        Film filmToTestGetAll1 = createFilm("Film1");
        Film filmToTestGetAll2 = createFilm("Film2");

        filmService.add(filmToTestGetAll1);
        filmService.add(filmToTestGetAll2);

        long id1 = 1;
        long id2 = 2;

        filmToTestGetAll1.setId(id1);
        filmToTestGetAll2.setId(id2);

        List<Film> films = filmService.getAll();

        assertEquals(2, films.size());
        assertEquals(filmToTestGetAll1, films.get(0));
        assertEquals(filmToTestGetAll2, films.get(1));
    }

    @Test
    @DisplayName("Тестирование добавления лайка.")
    void addLike() {
        Film film = createFilm("Add like test film.");
        filmService.add(film);
        filmService.addLike(1L, 5L);

        Film filmFromMemory = filmService.findFilm(1L);
        long userId = (long) filmFromMemory.getLikes().toArray()[0];
        int likesAmount = filmFromMemory.getLikes().size();

        assertEquals(1, likesAmount);
        assertEquals(5, userId);
    }

    @Test
    @DisplayName("Тестирование удаления лайка.")
    void removeLike() {
        Film film1 = createFilm("Remove like test film 1");

        filmService.add(film1);
        filmService.addLike(1L, 51L);
        filmService.addLike(1L, 666L);

        filmService.removeLike(1L, 51L);

        Film filmFromMemory = filmService.findFilm(1L);

        long userId = (long) filmFromMemory.getLikes().toArray()[0];
        int likesAmount = filmFromMemory.getLikes().size();

        assertEquals(1, likesAmount);
        assertEquals(666, userId);
    }

    @Test
    @DisplayName("Тестирование получения списка фильмов с наибольшим количествм лайков.")
    void getPopularFilms() {
//     Создаём 9 фильмов.
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String filmName = RandomStringUtils.randomAlphabetic(10);
            Film film = createFilm(filmName);
            filmService.add(film);
        }
//     Ставим им рандомное количество лайков.
        for (int i = 0; i < 100; i++) {
            long filmId = random.nextInt(8) + 1;
            long userId = random.nextInt(500);
            filmService.addLike(filmId, userId);
        }
//     Получаем count лучших фильмов.
        int count = 3;
        List<Film> popularFilms = filmService.getPopularFilms(count);

//     Сортируем все фильмы по количеству лайков для проверки.
        List<Film> films = filmService.getAll();
        List<Film> likesAmount = films.stream()
                .sorted(Comparator.comparingInt(film -> -film.getLikes().size()))
                .collect(Collectors.toList());

//     Проверяем количество полученных фильмов и сверяем с полученным ранее списком.

        int filmsAmount = popularFilms.size();

        assertEquals(count, filmsAmount);
        assertEquals(likesAmount.get(0), popularFilms.get(0));
        assertEquals(likesAmount.get(2), popularFilms.get(2));
    }

    Film createFilm(String filmName) {
        return Film.builder()
                .name(filmName)
                .description("Film for test")
                .duration(90)
                .releaseDate(LocalDate.of(1985, 12, 1))
                .genre(COMEDY.getGenre())
                .rating(PG13.getRating())
                .build();
    }
}