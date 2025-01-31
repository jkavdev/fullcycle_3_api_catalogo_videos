package br.com.jkavdev.fullcycle.catalogo.infrastructure.utils;

import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.InternalErrorException;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler;

import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface HttpClient {

    Predicate<HttpStatusCode> isNotFound = HttpStatus.NOT_FOUND::equals;

    Predicate<HttpStatusCode> is5xx = HttpStatusCode::is5xxServerError;

    String namespace();

    default ErrorHandler notFoundHandler(final String id) {
        return (req, res) -> {
            throw NotFoundException.with("not found observed from %s [resourceId::%s]".formatted(namespace(), id));
        };
    }

    default ErrorHandler a5xxHandler(final String id) {
        return (req, res) -> {
            throw InternalErrorException.with("error observed from %s [resourceId::%s] [status::%s]".
                    formatted(namespace(), id, res.getStatusCode().value()));
        };
    }

    default <T> Optional<T> getGet(final String id, final Supplier<T> fn) {
        try {
            return Optional.ofNullable(fn.get());
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            if (ExceptionUtils.getRootCause(ex) instanceof TimeoutException) {
                throw InternalErrorException.with("timeout observed from %s [resourceId::%s]".formatted(namespace(), id));
            }
            throw ex;
        }
    }

}
