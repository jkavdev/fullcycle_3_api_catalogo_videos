package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models;

import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record GenreDto(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("active") Boolean active,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {

    @Override
    public Boolean active() {
        return active != null ? active : true;
    }

    public Genre toGenre() {
        return Genre.with(id(), name(), active(), categories(), createdAt(), updatedAt(), deletedAt());
    }
}