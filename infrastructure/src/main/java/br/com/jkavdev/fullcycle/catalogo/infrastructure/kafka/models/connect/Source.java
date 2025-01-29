package br.com.jkavdev.fullcycle.catalogo.infrastructure.kafka.models.connect;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Source(
        @JsonProperty("name") String name,
        @JsonProperty("table") String table,
        @JsonProperty("db") String database
) {
}
