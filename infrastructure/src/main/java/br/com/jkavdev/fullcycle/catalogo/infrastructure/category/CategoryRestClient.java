package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.CategoryDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class CategoryRestClient {

    private final RestClient restClient;

    public CategoryRestClient(
            final RestClient categoryHttpClient
    ) {
        this.restClient = Objects.requireNonNull(categoryHttpClient);
    }

    public Optional<CategoryDto> getById(final String categoryId) {
        try {
            final var response = restClient
                    .get()
                    .uri("/{id}", categoryId)
                    .retrieve()
                    .onStatus(
                            HttpStatus.NOT_FOUND::equals, (req, res) -> {
                                throw NotFoundException.with("a category of id %s was not found".formatted(categoryId));
                            }
                    ).onStatus(
                            HttpStatusCode::is5xxServerError, (req, res) -> {
                                throw InternalErrorException.with("failed to get category of id %s".formatted(categoryId));
                            }
                    )
                    .body(CategoryDto.class);

            return Optional.ofNullable(response);
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            if (ExceptionUtils.getRootCause(ex) instanceof TimeoutException) {
                throw InternalErrorException.with("timeout from category of id %s".formatted(categoryId));
            }
            throw ex;
        }
    }

}
