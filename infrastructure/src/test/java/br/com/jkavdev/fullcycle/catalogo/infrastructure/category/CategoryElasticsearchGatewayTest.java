package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.AbstratcElasticsearchTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryElasticsearchGatewayTest extends AbstratcElasticsearchTest {

    @Autowired
    private CategoryElasticsearchRepository categoryRepository;

    @Test
    void injectionTest() {
        Assertions.assertNotNull(categoryRepository);
    }

}
