package br.com.jkavdev.fullcycle.catalogo.application.genre.get;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetAllByIdUseCase extends UseCase<GetAllByIdUseCase.Input, List<GetAllByIdUseCase.Output>> {

    private final GenreGateway genreGateway;

    public GetAllByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public List<Output> execute(Input input) {
        if (input.ids().isEmpty()) {
            return Collections.emptyList();
        }
        return genreGateway.findAllById(input.ids()).stream()
                .map(Output::new)
                .toList();
    }

    public record Input(List<String> ids) {
        @Override
        public List<String> ids() {
            return ids != null ? ids : Collections.emptyList();
        }
    }

    public record Output(
            String id,
            String name,
            boolean active,
            Set<String> categories,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ) {

        public Output(final Genre aGenre) {
            this(
                    aGenre.id(),
                    aGenre.name(),
                    aGenre.active(),
                    aGenre.categories(),
                    aGenre.createdAt(),
                    aGenre.updatedAt(),
                    aGenre.deletedAt()
            );
        }
    }

}
