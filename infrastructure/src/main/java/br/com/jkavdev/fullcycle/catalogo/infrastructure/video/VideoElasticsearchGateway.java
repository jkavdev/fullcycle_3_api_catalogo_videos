package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Profile("!development")
public class VideoElasticsearchGateway implements VideoGateway {

    private final VideoRepository videoRepository;

    private final SearchOperations searchOperations;

    public VideoElasticsearchGateway(
            final VideoRepository videoRepository,
            final SearchOperations searchOperations
    ) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Video save(final Video video) {
        videoRepository.save(VideoDocument.from(video));
        return video;
    }

    @Override
    public void deleteById(final String anId) {
        if (anId == null || anId.isBlank()) {
            return;
        }
        videoRepository.deleteById(anId);
    }

    @Override
    public Optional<Video> findById(String anId) {
        if (anId == null || anId.isBlank()) {
            return Optional.empty();
        }
        return videoRepository.findById(anId)
                .map(VideoDocument::toVideo);
    }

    @Override
    public Pagination<Video> findAll(VideoSearchQuery aQuery) {
        return null;
    }

}
