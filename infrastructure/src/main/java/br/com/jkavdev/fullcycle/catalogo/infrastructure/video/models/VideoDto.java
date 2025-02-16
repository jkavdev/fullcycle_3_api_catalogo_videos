package br.com.jkavdev.fullcycle.catalogo.infrastructure.video.models;

import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;

import java.util.Set;

public record VideoDto(
        String id,
        String title,
        String description,
        int yearLaunched,
        String rating,
        Double duration,
        boolean opened,
        boolean published,
        String banner,
        String thumbnail,
        String thumbnailHalf,
        String trailer,
        String video,
        Set<String> categoriesId,
        Set<String> castMembersId,
        Set<String> genresId,
        String createdAt,
        String updatedAt
) {

    public static VideoDto from(final Video video) {
        return new VideoDto(
                video.id(),
                video.title(),
                video.description(),
                video.launchedAt().getValue(),
                video.rating().getName(),
                video.duration(),
                video.opened(),
                video.published(),
                video.banner(),
                video.thumbnail(),
                video.thumbnailHalf(),
                video.trailer(),
                video.video(),
                video.categories(),
                video.castMembers(),
                video.genres(),
                video.createdAt().toString(),
                video.updatedAt().toString()
        );
    }
}
