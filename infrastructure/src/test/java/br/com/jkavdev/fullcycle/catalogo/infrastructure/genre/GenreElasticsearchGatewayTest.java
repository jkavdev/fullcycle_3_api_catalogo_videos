package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.IdUtils;
import br.com.jkavdev.fullcycle.catalogo.domain.utils.InstantUtils;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
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

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var expectedGenre = Fixture.Genres.business();

        genreRepository.save(GenreDocument.from(expectedGenre));

        final var expectedId = expectedGenre.id();
        Assertions.assertTrue(genreRepository.existsById(expectedId));

        // when
        genreGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(genreRepository.existsById(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> genreGateway.deleteById(expectedId));
    }

    @Test
    public void givenActiveGenreWithCategories_whenCallsFindById_shouldRetrieveIt() {
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

        genreRepository.save(GenreDocument.from(expectedGenre));

        final var expectedId = expectedGenre.id();
        final var actualOutput = genreRepository.findById(expectedId)
                .orElseThrow();

        // when
        genreGateway.findById(expectedId);

        // then
        Assertions.assertEquals(expectedGenre.id(), actualOutput.id());
        Assertions.assertEquals(expectedGenre.name(), actualOutput.name());
        Assertions.assertEquals(expectedGenre.active(), actualOutput.active());
        Assertions.assertEquals(expectedGenre.categories(), actualOutput.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenInactiveGenreWithoutCategories_whenCallsFindById_shouldRetrieveIt() {
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

        genreRepository.save(GenreDocument.from(expectedGenre));

        final var expectedId = expectedGenre.id();
        final var actualOutput = genreRepository.findById(expectedId)
                .orElseThrow();

        // when
        genreGateway.findById(expectedId);

        // then
        Assertions.assertEquals(expectedGenre.id(), actualOutput.id());
        Assertions.assertEquals(expectedGenre.name(), actualOutput.name());
        Assertions.assertEquals(expectedGenre.active(), actualOutput.active());
        Assertions.assertEquals(expectedGenre.categories(), actualOutput.categories());
        Assertions.assertEquals(expectedGenre.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(expectedGenre.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(expectedGenre.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldReturnEmpy() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertTrue(genreGateway.findById(expectedId).isEmpty());
    }

}