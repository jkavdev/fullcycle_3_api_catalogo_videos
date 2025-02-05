package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CastMemberDto(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {

    public CastMember toCastMember() {
        return CastMember.with(id(), name(), CastMemberType.of(type), createdAt(), updatedAt());
    }
}