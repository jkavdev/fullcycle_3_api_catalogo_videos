package br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember;

import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMember;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CastMemberElasticsearchGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberElasticsearchGateway(
            final CastMemberRepository castMemberRepository
    ) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember save(final CastMember aMember) {
        castMemberRepository.save(CastMemberDocument.from(aMember));
        return aMember;
    }

    @Override
    public void deleteById(String anId) {

    }

    @Override
    public Optional<CastMember> findById(String anId) {
        return Optional.empty();
    }

    @Override
    public Pagination<CastMember> findAll(CastMemberSearchQuery aQuery) {
        return null;
    }

}
