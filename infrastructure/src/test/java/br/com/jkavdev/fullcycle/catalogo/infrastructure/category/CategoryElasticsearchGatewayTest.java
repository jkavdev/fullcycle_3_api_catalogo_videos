package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
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
}
