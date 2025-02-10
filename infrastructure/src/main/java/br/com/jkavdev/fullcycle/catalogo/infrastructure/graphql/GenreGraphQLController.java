package br.com.jkavdev.fullcycle.catalogo.infrastructure.graphql;

import br.com.jkavdev.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.application.genre.save.SaveGenreUseCase;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.models.GenreDto;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class GenreGraphQLController {

    private final ListGenreUseCase listGenreUseCase;

    private final SaveGenreUseCase saveGenreUseCase;

    public GenreGraphQLController(
            final ListGenreUseCase listGenreUseCase,
            final SaveGenreUseCase saveGenreUseCase
    ) {
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
    }

    @QueryMapping
    public List<ListGenreUseCase.Output> genres(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final Set<String> categories
    ) {
        final var input =
                new ListGenreUseCase.Input(page, perPage, search, sort, direction, categories);

        return listGenreUseCase.execute(input).data();
    }

    @MutationMapping
    public SaveGenreUseCase.Output saveGenre(@Argument(name = "input") final GenreDto arg) {
        final var input = new SaveGenreUseCase.Input(
                arg.id(), arg.name(), arg.active(), arg.categories(), arg.createdAt(), arg.updatedAt(), arg.deletedAt()
        );
        return saveGenreUseCase.execute(input);
    }

}
