package br.com.jkavdev.fullcycle.catalogo.application.video.list;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoSearchQuery;

import java.util.Objects;
import java.util.Set;

public class ListVideoUseCase extends UseCase<ListVideoUseCase.Input, Pagination<ListVideoUseCase.Output>> {

    private final VideoGateway videoGateway;

    public ListVideoUseCase(final VideoGateway genreGateway) {
        this.videoGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<Output> execute(final Input input) {
        final var aQuery = new VideoSearchQuery(
                input.page(),
                input.perPage(),
                input.terms(),
                input.sort(),
                input.direction(),
                input.rating(),
                input.launchedAt(),
                input.categories(),
                input.castMembers(),
                input.genres()
        );

        return videoGateway.findAll(aQuery)
                .map(Output::from);
    }

    public record Input(
            int page,
            int perPage,
            String terms,
            String sort,
            String direction,
            String rating,
            Integer launchedAt,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
    ) {

    }

    public record Output(
            String id,
            String title,
            boolean published,
            int yearLaunched,
            String rating,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
    ) {

        public static Output from(final Video video) {
            return new Output(
                    video.id(),
                    video.title(),
                    video.published(),
                    video.launchedAt().getValue(),
                    video.rating().getName(),
                    video.categories(),
                    video.castMembers(),
                    video.genres()
            );
        }
    }

}
