package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.AbstractElasticsearchTest;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(categoryRepository);
    }
}
