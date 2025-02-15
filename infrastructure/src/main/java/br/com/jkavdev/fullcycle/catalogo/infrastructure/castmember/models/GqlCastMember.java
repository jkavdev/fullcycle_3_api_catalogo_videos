package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models;

import java.time.Instant;

public record GqlCastMember(
        String id,
        String name,
        String type,
        Instant createdAt,
        Instant updatedAt
) {
}