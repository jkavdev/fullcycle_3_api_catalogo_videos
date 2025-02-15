package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.application.castmember.list.ListCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.castmember.save.SaveCastMemberUseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.castmember.CastMemberSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.GqlCastMemberPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMemberDto;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMember;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class CastMemberGraphQLController {

    private final ListCastMemberUseCase listCastMemberUseCase;

    private final SaveCastMemberUseCase saveCastMemberUseCase;

    public CastMemberGraphQLController(
            final ListCastMemberUseCase listCastMemberUseCase,
            final SaveCastMemberUseCase saveCastMemberUseCase
    ) {
        this.listCastMemberUseCase = Objects.requireNonNull(listCastMemberUseCase);
        this.saveCastMemberUseCase = Objects.requireNonNull(saveCastMemberUseCase);
    }

    @QueryMapping
    public List<GqlCastMember> castMembers(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction
    ) {
        final var aQuery =
                new CastMemberSearchQuery(page, perPage, search, sort, direction);

        return listCastMemberUseCase.execute(aQuery)
                .map(GqlCastMemberPresenter::present)
                .data();
    }

    @MutationMapping
    public GqlCastMember saveCastMember(@Argument final GqlCastMemberDto input) {
        return GqlCastMemberPresenter.present(saveCastMemberUseCase.execute(input.toCastMember()));
    }

}
