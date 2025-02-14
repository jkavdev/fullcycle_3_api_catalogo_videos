package br.com.jkavdev.fullcycle.catalogo.application.video.save;

import br.com.jkavdev.fullcycle.catalogo.application.UseCase;
import br.com.jkavdev.fullcycle.catalogo.domain.exceptions.DomainException;
import br.com.jkavdev.fullcycle.catalogo.domain.validation.Error;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Rating;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;

import java.time.Instant;
import java.time.Year;
import java.util.Objects;
import java.util.Set;

public class SaveVideoUseCase extends UseCase<SaveVideoUseCase.Input, SaveVideoUseCase.Output> {

    private final VideoGateway videoGateway;

    public SaveVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw DomainException.with(new Error("'SaveVideoUseCase.Input' cannot be null"));
        }
        final var video = Video.with(
                input.id(),
                input.title(),
                input.description(),
                input.launchedAt().getValue(),
                input.duration(),
                input.rating().getName(),
                input.opened(),
                input.published(),
                input.createdAt().toString(),
                input.updatedAt().toString(),
                input.banner(),
                input.thumbnail(),
                input.thumbnailHalf(),
                input.trailer(),
                input.video(),
                input.categories(),
                input.castMembers(),
                input.genres()
        );

        videoGateway.save(video);

        return new Output(video.id());
    }

    public record Input(
            String id,
            String title,
            String description,
            Year launchedAt,
            double duration,
            Rating rating,
            boolean opened,
            boolean published,
            Instant createdAt,
            Instant updatedAt,
            String banner,
            String thumbnail,
            String thumbnailHalf,
            String trailer,
            String video,
            Set<String> categories,
            Set<String> castMembers,
            Set<String> genres
    ) {

    }

    public record Output(
            String id
    ) {

    }

}
