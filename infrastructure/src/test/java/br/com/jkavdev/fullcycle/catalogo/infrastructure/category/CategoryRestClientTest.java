package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.Fixture;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.AbstractRestClientTest;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Map;

public class CategoryRestClientTest extends AbstractRestClientTest {

    @Autowired
    private CategoryRestClient target;

    // OK
    @Test
    public void givenACategory_whenReceive200FromServer_shouldBeOk() {
        // given
        final var aulas = Fixture.Categories.aulas();

        final var responseBody = writeValueAsString(new CategoryDto(
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

        WireMock.verify(1, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
        );
    }

    // 5XX
    @Test
    public void givenACategory_whenReceive5XXFromServer_shouldReturnInternalError() {
        // given
        final var expectedId = "123";
        final var expectedErrorStatus = 500;
        final var expecterErrorMessage = "error observed from categories [resourceId::%s] [status::%s]"
                .formatted(expectedId, expectedErrorStatus);

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        WireMock.stubFor(
                WireMock.get(
                                WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(expectedErrorStatus)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualException = Assertions.assertThrows(InternalErrorException.class,
                () -> target.getById(expectedId));

        // then
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        WireMock.verify(2, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
        );
    }

    // 404
    @Test
    public void givenACategory_whenReceive404NotFoundFromServer_shouldReturnEmpty() {
        // given
        final var expectedId = "123";
        final var responseBody = writeValueAsString(Map.of("message", "not found"));

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

        WireMock.verify(1, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
        );
    }

    // timeout
    @Test
    public void givenACategory_whenReceiveTimeout_shouldReturnInternalError() {
        // given
        final var aulas = Fixture.Categories.aulas();

        final var responseBody = writeValueAsString(new CategoryDto(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.active(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        ));
        final var expecterErrorMessage = "timeout observed from categories [resourceId::%s]".formatted(aulas.id());

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

        WireMock.verify(2, WireMock.getRequestedFor(
                WireMock.urlPathEqualTo("/api/categories/%s".formatted(aulas.id())))
        );
    }

    // bulkhead
    @Test
    public void givenACategory_whenBulkheadIsFull_shouldReturnError() {
        // given
        final var expectedId = "123";
        final var expecterErrorMessage = "Bulkhead 'categories' is full and does not permit further calls";

        acquireBulkheadPermission(CATEGORY);

        // when
        final var actualException = Assertions.assertThrows(BulkheadFullException.class,
                () -> target.getById(expectedId));

        // then
        // nas configuracoes de testes definimos o maximo de permissoes por chamada com o valor::1
        // ai ao da errado ja tendo uma permissao, vai da o erro especifico do bulhead
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        releaseBulkheadPermission(CATEGORY);
    }

    @Test
    public void givenServerError_whenIsMoreThanThreshold_shouldOpenCircuitBreaker() {
        // given
        final var expectedId = "123";
        final var expectedErrorStatus = 500;
        final var expecterErrorMessage = "CircuitBreaker 'categories' is OPEN and does not permit further calls";

        final var responseBody = writeValueAsString(Map.of("message", "Internal Server Error"));

        WireMock.stubFor(
                WireMock.get(
                                WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId)))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(expectedErrorStatus)
                                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(responseBody)
                        )
        );

        // when
        final var actualException = Assertions.assertThrows(CallNotPermittedException.class, () -> target.getById(expectedId));

        // then
        checkCircuitBreakerState(CATEGORY, CircuitBreaker.State.OPEN);
        Assertions.assertEquals(expecterErrorMessage, actualException.getMessage());

        WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }

}

