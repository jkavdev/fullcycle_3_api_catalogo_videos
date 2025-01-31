package br.com.jkavdev.fullcycle.catalogo.infrastructure;

import br.com.jkavdev.fullcycle.catalogo.IntegrationTestConfiguration;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.WebServerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test-integration")
@AutoConfigureWireMock(port = 0)
@EnableAutoConfiguration(
        exclude = {
                ElasticsearchRepositoriesAutoConfiguration.class,
                KafkaAutoConfiguration.class
        }
)
@SpringBootTest(
        classes = {
                WebServerConfig.class,
                IntegrationTestConfiguration.class
        }
)
@Tag("integrationTests")
public abstract class AbstractRestClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    protected String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
