package ch.martinelli.sakila.dto;

import java.util.List;

public record ActorWithFilms(Integer id, String firstName, String lastName, List<FilmName> films) {

    public record FilmName(String name) {
    }
}
