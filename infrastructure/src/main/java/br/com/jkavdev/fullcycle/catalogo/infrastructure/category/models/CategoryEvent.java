package br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CategoryEvent(
        @JsonProperty("id") String id
) {

}
