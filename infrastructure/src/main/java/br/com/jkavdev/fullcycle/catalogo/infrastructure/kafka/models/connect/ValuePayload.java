package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ValuePayload<T>(
        @JsonProperty("after") T after,
        @JsonProperty("before") T before,
        @JsonProperty("source") Source source,
        @JsonProperty("operation") Operation operation
) {

}