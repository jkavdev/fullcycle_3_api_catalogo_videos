package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.utils.HttpClient;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Component
public class CategoryRestClient implements HttpClient {

    public static final String NAMESPACE = "categories";

    private final RestClient restClient;

    public CategoryRestClient(
            final RestClient categoryHttpClient
    ) {
        this.restClient = Objects.requireNonNull(categoryHttpClient);
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }

    @Retry(name = NAMESPACE)
    public Optional<CategoryDto> getById(final String categoryId) {
        return getGet(categoryId, () -> restClient
                .get()
                .uri("/{id}", categoryId)
                .retrieve()
                .onStatus(isNotFound, notFoundHandler(categoryId))
                .onStatus(is5xx, a5xxHandler(categoryId))
                .body(CategoryDto.class));
    }

}
