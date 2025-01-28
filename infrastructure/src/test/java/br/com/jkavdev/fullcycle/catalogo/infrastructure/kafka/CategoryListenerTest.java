package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka;

import br.com.jkavdev.fullcycle.catalogo.AbstractEmbeddedKafkaTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryListenerTest extends AbstractEmbeddedKafkaTest {

    @Test
    public void test() {
        Assertions.assertNotNull(producer());
    }

}
