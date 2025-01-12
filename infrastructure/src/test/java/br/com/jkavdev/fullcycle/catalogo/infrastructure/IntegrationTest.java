package br.com.jkavdev.fullcycle.catalogo.infrastructure;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

// indicando que sera usada nas classes de testes
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
// definindo profile de teste
@ActiveProfiles("test-integration")
// para testes integrados precisamos do spring com todo o seu contexto
@SpringBootTest(classes = WebServerConfig.class)
@Tag("integrationTests")
public @interface IntegrationTest {

}