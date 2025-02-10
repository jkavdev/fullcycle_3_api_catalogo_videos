package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

class GenreElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private GenreElasticsearchGateway genreGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(genreRepository);
        Assertions.assertNotNull(genreGateway);
    }

    @Test
    public void givenActiveGenreWithCategories_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedGenre = Genre.with(
                IdUtils.uniqueId(),
                "Business",
                true,
                Set.of("c1", "c2"),
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        // when
        final var actualOutput = genreGateway.save(expectedGenre);

        // then
        Assertions.assertEquals(expectedGenre, actualOutput);

        final var actualGenre = genreRepository.findById(expectedGenre.id())
                .orElseThrow();
        Assertions.assertEquals(expectedGenre.id(), actualGenre.id());
        Assertions.assertEquals(expectedGenre.name(), actualGenre.name());
        Assertions.assertEquals(expectedGenre.active(), actualGenre.active());
        Assertions.assertEquals(expectedGenre.categories(), actualGenre.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenInactiveGenreWithoutCategories_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedGenre = Genre.with(
                IdUtils.uniqueId(),
                "Business",
                false,
                new HashSet<>(),
                InstantUtils.now(),
                InstantUtils.now(),
                InstantUtils.now()
        );

        // when
        final var actualOutput = genreGateway.save(expectedGenre);

        // then
        Assertions.assertEquals(expectedGenre, actualOutput);

        final var actualGenre = genreRepository.findById(expectedGenre.id())
                .orElseThrow();
        Assertions.assertEquals(expectedGenre.id(), actualGenre.id());
        Assertions.assertEquals(expectedGenre.name(), actualGenre.name());
        Assertions.assertEquals(expectedGenre.active(), actualGenre.active());
        Assertions.assertEquals(expectedGenre.categories(), actualGenre.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualGenre.deletedAt());
    }

}