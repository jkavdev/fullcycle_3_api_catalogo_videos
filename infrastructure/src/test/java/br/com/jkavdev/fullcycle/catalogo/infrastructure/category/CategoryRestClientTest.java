package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.IntegrationTestConfiguration;
import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.WebServerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

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
public class CategoryRestClientTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRestClient target;

    // OK
    @Test
    public void givenACategory_whenReceive200FromServer_shouldBeOk() throws JsonProcessingException {
        // given
        final var aulas = Fixture.Categories.aulas();

        final var responseBody = objectMapper.writeValueAsString(new CategoryDto(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.active(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        ));

        WireMock.stubFor(
                WireMock.get(
                                WireMock.urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualCategory = target.getById(aulas.id()).get();

        // then
        Assertions.assertEquals(aulas.id(), actualCategory.id());
        Assertions.assertEquals(aulas.name(), actualCategory.name());
        Assertions.assertEquals(aulas.description(), actualCategory.description());
        Assertions.assertEquals(aulas.active(), actualCategory.active());
        Assertions.assertEquals(aulas.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aulas.deletedAt(), actualCategory.deletedAt());
    }

    // 5XX
    @Test
    public void givenACategory_whenReceive5XXFromServer_shouldReturnInternalError() throws JsonProcessingException {
        // given
        final var expectedId = "123";
        final var expecterErrorMessage = "failed to get category of id %s".formatted(expectedId);

        final var responseBody = objectMapper.writeValueAsString(Map.of("message", "Internal Server Error"));

        WireMock.stubFor(
                WireMock.get(
                                WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(500)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualException = Assertions.assertThrows(InternalErrorException.class,
                () -> target.getById(expectedId));

        // then
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());
    }

    // 404
    @Test
    public void givenACategory_whenReceive404NotFoundFromServer_shouldReturnEmpty() throws JsonProcessingException {
        // given
        final var expectedId = "123";
        final var responseBody = objectMapper.writeValueAsString(Map.of("message", "not found"));

        WireMock.stubFor(
                WireMock.get(
                                WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(404)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualCategory = target.getById(expectedId);

        // then
        Assertions.assertTrue(actualCategory.isEmpty());
    }

    // timeout
    @Test
    public void givenACategory_whenReceiveTimeout_shouldReturnInternalError() throws JsonProcessingException {
        // given
        final var aulas = Fixture.Categories.aulas();

        final var responseBody = objectMapper.writeValueAsString(new CategoryDto(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.active(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        ));
        final var expecterErrorMessage = "timeout from category of id %s".formatted(aulas.id());

        WireMock.stubFor(
                WireMock.get(
                                WireMock.urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withFixedDelay(600)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualException = Assertions.assertThrows(InternalErrorException.class,
                () -> target.getById(aulas.id()));

        // then
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());
    }

}

