package br.com.jkavdev.fullcycle.catalogo.domain.pagination;

public record Metada(
        int currentPage,
        int perPage,
        long total
) {

}
