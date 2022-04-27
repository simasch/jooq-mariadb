package ch.martinelli.sakila;

import ch.martinelli.sakila.tables.daos.FilmDao;
import ch.martinelli.sakila.tables.pojos.Film;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class DaoTest {

    @Autowired
    private DSLContext dsl;

    @Test
    void find_by_id() {
        FilmDao filmDao = new FilmDao(dsl.configuration());

        Film film = filmDao.findById(1);

        assertThat(film).isNotNull();
    }

    @Test
    void fetch_optional_by_film_id() {
        FilmDao filmDao = new FilmDao(dsl.configuration());

        Optional<Film> film = filmDao.fetchOptionalByFilmId(1);

        assertThat(film).isPresent();
    }

    @Test
    void fetch_by_title() {
        FilmDao filmDao = new FilmDao(dsl.configuration());

        List<Film> films = filmDao.fetchByTitle("ACE GOLDFINGER");

        assertThat(films).hasSize(1);
    }
}
