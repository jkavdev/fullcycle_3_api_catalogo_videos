package br.com.jkavdev.fullcycle.catalogo.domain.genre;

public record GenreSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
