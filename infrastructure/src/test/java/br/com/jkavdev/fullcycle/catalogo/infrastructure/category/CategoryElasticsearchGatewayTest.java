package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGateway categoryGateway;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(categoryRepository);
        Assertions.assertNotNull(categoryGateway);
    }

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        // given
        final var expectedCategory = Fixture.Categories.aulas();

        // when
        final var actualOutput = categoryGateway.save(expectedCategory);

        // then
        Assertions.assertEquals(expectedCategory, actualOutput);

        final var actualCategory = categoryRepository.findById(expectedCategory.id())
                .orElseThrow();
        Assertions.assertEquals(expectedCategory.id(), actualCategory.id());
        Assertions.assertEquals(expectedCategory.name(), actualCategory.name());
        Assertions.assertEquals(expectedCategory.description(), actualCategory.description());
        Assertions.assertEquals(expectedCategory.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(expectedCategory.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(expectedCategory.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var expectedCategory = Fixture.Categories.aulas();

        categoryRepository.save(CategoryDocument.from(expectedCategory));

        final var expectedId = expectedCategory.id();
        Assertions.assertTrue(categoryRepository.existsById(expectedId));

        // when
        categoryGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(categoryRepository.existsById(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertDoesNotThrow(() -> categoryGateway.deleteById(expectedId));
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var expectedCategory = Fixture.Categories.talks();

        categoryRepository.save(CategoryDocument.from(expectedCategory));

        final var expectedId = expectedCategory.id();
        final var actualOutput = categoryRepository.findById(expectedId)
                .orElseThrow();

        // when
        categoryGateway.deleteById(expectedId);

        // then
        Assertions.assertEquals(expectedCategory.id(), actualOutput.id());
        Assertions.assertEquals(expectedCategory.name(), actualOutput.name());
        Assertions.assertEquals(expectedCategory.description(), actualOutput.description());
        Assertions.assertEquals(expectedCategory.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(expectedCategory.updatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(expectedCategory.deletedAt(), actualOutput.deletedAt());
    }

    @Test
    public void givenValidId_whenCallsFindById_shouldReturnEmpy() {
        // given
        final var expectedId = "qualquerId";

        // when
        // then
        Assertions.assertTrue(categoryGateway.findById(expectedId).isEmpty());
    }

}
