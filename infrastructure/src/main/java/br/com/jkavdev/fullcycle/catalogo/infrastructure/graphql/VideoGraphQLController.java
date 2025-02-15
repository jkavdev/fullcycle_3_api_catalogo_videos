package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.application.castmember.get.GetAllCastMemberByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.get.GetAllCategoryByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.get.GetAllGenreByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.GqlCastMemberPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.castmember.models.GqlCastMember;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.GqlCategoryPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.models.GqlCategory;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.GqlGenrePresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GqlGenre;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.GqlVideoPresenter;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models.GqlVideo;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class VideoGraphQLController {

    private final ListVideoUseCase listVideoUseCase;

    private final GetAllCastMemberByIdUseCase getAllCastMemberByIdUseCase;

    private final GetAllCategoryByIdUseCase getAllCategoryByIdUseCase;

    private final GetAllGenreByIdUseCase getAllGenreByIdUseCase;

    public VideoGraphQLController(
            final ListVideoUseCase listVideoUseCase,
            final GetAllCastMemberByIdUseCase getAllCastMemberByIdUseCase,
            final GetAllCategoryByIdUseCase getAllCategoryByIdUseCase,
            final GetAllGenreByIdUseCase getAllGenreByIdUseCase
    ) {
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.getAllCastMemberByIdUseCase = Objects.requireNonNull(getAllCastMemberByIdUseCase);
        this.getAllCategoryByIdUseCase = Objects.requireNonNull(getAllCategoryByIdUseCase);
        this.getAllGenreByIdUseCase = Objects.requireNonNull(getAllGenreByIdUseCase);
    }

    @QueryMapping
    public List<GqlVideo> videos(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final String rating,
            @Argument final Integer yearLaunched,
            @Argument final Set<String> castMembers,
            @Argument final Set<String> categories,
            @Argument final Set<String> genres
    ) {
        final var input =
                new ListVideoUseCase.Input(
                        page, perPage, search, sort, direction, rating, yearLaunched, castMembers, categories, genres
                );

        return listVideoUseCase.execute(input)
                .map(GqlVideoPresenter::present)
                .data();
    }

    @SchemaMapping(typeName = "Video", field = "castMembers")
    public List<GqlCastMember> castMembers(final GqlVideo video) {
        return getAllCastMemberByIdUseCase.execute(
                        new GetAllCastMemberByIdUseCase.Input(video.castMembersId())
                ).stream()
                .map(GqlCastMemberPresenter::present)
                .toList();
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    public List<GqlCategory> categories(final GqlVideo video) {
        return getAllCategoryByIdUseCase.execute(
                        new GetAllCategoryByIdUseCase.Input(video.categoriesId())
                ).stream()
                .map(GqlCategoryPresenter::present)
                .toList();
    }

    @SchemaMapping(typeName = "Video", field = "genres")
    public List<GqlGenre> genres(final GqlVideo video) {
        return getAllGenreByIdUseCase.execute(
                        new GetAllGenreByIdUseCase.Input(video.genresId())
                ).stream()
                .map(GqlGenrePresenter::present)
                .toList();
    }

}
