package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models;

import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;

import java.time.Instant;
import java.util.Set;

public record GenreDto(
        String id,
        String name,
        Boolean active,
        Set<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    @Override
    public Boolean active() {
        return active != null ? active : true;
    }

    public Genre toGenre() {
        return Genre.with(id(), name(), active(), categories(), createdAt(), updatedAt(), deletedAt());
    }
}