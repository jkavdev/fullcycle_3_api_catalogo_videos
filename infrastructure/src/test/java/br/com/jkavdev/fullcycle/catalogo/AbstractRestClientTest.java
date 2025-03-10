package br.com.jkavdev.fullcycle.catalogo;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.CategoryRestClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.configuration.WebServerConfig;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.GenreRestClient;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.VideoRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

    protected static final String CATEGORY = CategoryRestClient.NAMESPACE;
    protected static final String GENRE = GenreRestClient.NAMESPACE;
    protected static final String VIDEO = VideoRestClient.NAMESPACE;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BulkheadRegistry bulkheadRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private CacheManager cacheManager;

    // evitando que o contexto de um teste interfira em outro teste, ai limpamos tudo
    @BeforeEach
    void beforeEach() {
        WireMock.reset();
        WireMock.resetAllRequests();
        resetAllCaches();
        List.of(CATEGORY, GENRE, VIDEO).forEach(this::resetFaultTolerance);
    }

    protected Cache cache(final String name) {
        return cacheManager.getCache(name);
    }

    protected void checkCircuitBreakerState(final String name, final CircuitBreaker.State expectedState) {
        final var cb = circuitBreakerRegistry.circuitBreaker(name);
        Assertions.assertEquals(expectedState, cb.getState());
    }

    protected void acquireBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).acquirePermission();
    }

    protected void releaseBulkheadPermission(final String name) {
        bulkheadRegistry.bulkhead(name).releasePermission();
    }

    protected void transitionToOpenState(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).transitionToOpenState();
    }

    protected void transitionToClosedState(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).transitionToClosedState();
    }

    protected String writeValueAsString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetAllCaches() {
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    }

    private void resetFaultTolerance(final String name) {
        circuitBreakerRegistry.circuitBreaker(name).reset();
    }
}
