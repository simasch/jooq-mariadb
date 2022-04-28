package ch.martinelli.sakila;

import ch.martinelli.sakila.dto.ActorWithFilms;
import ch.martinelli.sakila.dto.ActorWithFirstAndLastName;
import ch.martinelli.sakila.routines.GetCustomerBalance;
import ch.martinelli.sakila.tables.records.FilmListRecord;
import ch.martinelli.sakila.tables.records.FilmRecord;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static ch.martinelli.sakila.tables.Actor.ACTOR;
import static ch.martinelli.sakila.tables.Category.CATEGORY;
import static ch.martinelli.sakila.tables.Film.FILM;
import static ch.martinelli.sakila.tables.FilmActor.FILM_ACTOR;
import static ch.martinelli.sakila.tables.FilmCategory.FILM_CATEGORY;
import static ch.martinelli.sakila.tables.FilmList.FILM_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multisetAgg;

@Transactional
@SpringBootTest
class CrudTest {

    @Autowired
    private DSLContext dsl;

    @Test
    void find_all_films() {
        Result<FilmRecord> films = dsl
                .selectFrom(FILM)
                .fetch();

        assertThat(films).hasSize(1000);
    }

    @Test
    void find_film_by_id() {
        FilmRecord filmRecord = dsl
                .selectFrom(FILM)
                .where(FILM.FILM_ID.eq(1))
                .fetchOne();

        assertThat(filmRecord).isNotNull();
        assertThat(filmRecord.getTitle()).isEqualTo("ACADEMY DINOSAUR");
    }

    @Test
    void find_all_actors_of_horror_films() {
        Result<Record2<String, String>> actorsOfHorrorFilms = dsl
                .select(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .from(ACTOR)
                .join(FILM_ACTOR).on(FILM_ACTOR.ACTOR_ID.eq(ACTOR.ACTOR_ID))
                .join(FILM).on(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
                .join(FILM_CATEGORY).on(FILM_CATEGORY.FILM_ID.eq(FILM.FILM_ID))
                .join(CATEGORY).on(FILM_CATEGORY.CATEGORY_ID.eq(CATEGORY.CATEGORY_ID))
                .where(CATEGORY.NAME.eq("Horror"))
                .groupBy(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .orderBy(ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .fetch();


        assertThat(actorsOfHorrorFilms).hasSize(155);
    }

    @Test
    void find_all_actors_of_horror_films_implicit_join() {
        Result<Record2<String, String>> actorsOfHorrorFilms = dsl
                .select(FILM_ACTOR.actor().FIRST_NAME, FILM_ACTOR.actor().LAST_NAME)
                .from(FILM_ACTOR)
                .join(FILM_CATEGORY).on(FILM_ACTOR.FILM_ID.eq(FILM_CATEGORY.FILM_ID))
                .where(FILM_CATEGORY.category().NAME.eq("Horror"))
                .groupBy(FILM_ACTOR.actor().FIRST_NAME, FILM_ACTOR.actor().LAST_NAME)
                .orderBy(FILM_ACTOR.actor().FIRST_NAME, FILM_ACTOR.actor().LAST_NAME)
                .fetch();

        assertThat(actorsOfHorrorFilms).hasSize(155);
    }

    @Test
    void find_all_actors_of_horror_films_implicit_join_into_dto() {
        List<ActorWithFirstAndLastName> actorsOfHorrorFilms = dsl
                .select(FILM_ACTOR.actor().FIRST_NAME, FILM_ACTOR.actor().LAST_NAME)
                .from(FILM_ACTOR)
                .join(FILM_CATEGORY).on(FILM_ACTOR.FILM_ID.eq(FILM_CATEGORY.FILM_ID))
                .where(FILM_CATEGORY.category().NAME.eq("Horror"))
                .groupBy(FILM_ACTOR.actor().FIRST_NAME, FILM_ACTOR.actor().LAST_NAME)
                .orderBy(FILM_ACTOR.actor().FIRST_NAME, FILM_ACTOR.actor().LAST_NAME)
                .fetchInto(ActorWithFirstAndLastName.class);

        assertThat(actorsOfHorrorFilms).hasSize(155);
    }

    @Test
    void find_all_films_with_view() {
        Result<FilmListRecord> films = dsl
                .selectFrom(FILM_LIST)
                .fetch();

        assertThat(films).hasSize(997);
    }

    @Test
    void insert_film() {
        int insertedRows = dsl.
                insertInto(FILM)
                .columns(FILM.TITLE, FILM.LANGUAGE_ID)
                .values("Test", 1)
                .execute();

        assertThat(insertedRows).isEqualTo(1);
    }

    @Test
    void insert_film_using_record() {
        FilmRecord filmRecord = dsl.newRecord(FILM);
        filmRecord.setTitle("Test");
        filmRecord.setLanguageId(1);
        int insertedRows = filmRecord.store();

        assertThat(insertedRows).isEqualTo(1);
    }

    @Test
    void update_film() {
        int updatedRows = dsl.
                update(FILM)
                .set(FILM.RENTAL_RATE, new BigDecimal("10.00"))
                .where(FILM.FILM_ID.eq(1))
                .execute();

        assertThat(updatedRows).isEqualTo(1);
    }

    @Test
    void update_film_using_record() {
        FilmRecord filmRecord = dsl
                .selectFrom(FILM)
                .where(FILM.FILM_ID.eq(1))
                .fetchOne();

        filmRecord.setRentalRate(new BigDecimal("10.00"));
        int stored = filmRecord.store();

        assertThat(stored).isEqualTo(1);
    }

    @Test
    void delete_film() {
        assertThatThrownBy(() -> dsl
                .deleteFrom(FILM)
                .where(FILM.FILM_ID.eq(1))
                .execute())
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void get_customer_balance() {
        GetCustomerBalance getCustomerBalance = new GetCustomerBalance();
        getCustomerBalance.setPCustomerId(1);
        getCustomerBalance.setPEffectiveDate(LocalDateTime.of(2005, 5, 25, 0, 0));
        getCustomerBalance.execute(dsl.configuration());

        BigDecimal balance = getCustomerBalance.getReturnValue();

        assertThat(balance).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void find_all_actors_with_films() {
        List<ActorWithFilms> actorWithFilms = dsl
                .select(ACTOR.ACTOR_ID,
                        ACTOR.FIRST_NAME,
                        ACTOR.LAST_NAME,
                        multisetAgg(FILM_ACTOR.film().TITLE)
                                .convertFrom(r -> r.map(mapping(ActorWithFilms.FilmName::new)))
                )
                .from(ACTOR)
                .leftOuterJoin(FILM_ACTOR).on(FILM_ACTOR.ACTOR_ID.eq(ACTOR.ACTOR_ID))
                .groupBy(ACTOR.ACTOR_ID, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .fetch(mapping(ActorWithFilms::new));

        assertThat(actorWithFilms).hasSize(200);
    }
}
