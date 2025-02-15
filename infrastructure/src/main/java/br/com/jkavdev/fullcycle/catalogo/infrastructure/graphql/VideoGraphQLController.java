package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.application.castmember.get.GetAllCastMemberByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.category.get.GetAllCategoryByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.get.GetAllGenreByIdUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.video.list.ListVideoUseCase;
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
    public List<ListVideoUseCase.Output> videos(
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

        return listVideoUseCase.execute(input).data();
    }

    @SchemaMapping(typeName = "Video", field = "castMembers")
    public List<GetAllCastMemberByIdUseCase.Output> castMembers(final ListVideoUseCase.Output video) {
        return getAllCastMemberByIdUseCase.execute(new GetAllCastMemberByIdUseCase.Input(video.castMembersId()));
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    public List<GetAllCategoryByIdUseCase.Output> categories(final ListVideoUseCase.Output video) {
        return getAllCategoryByIdUseCase.execute(new GetAllCategoryByIdUseCase.Input(video.categoriesId()));
    }

    @SchemaMapping(typeName = "Video", field = "genres")
    public List<GetAllGenreByIdUseCase.Output> genres(final ListVideoUseCase.Output video) {
        return getAllGenreByIdUseCase.execute(new GetAllGenreByIdUseCase.Input(video.genresId()));
    }

}
