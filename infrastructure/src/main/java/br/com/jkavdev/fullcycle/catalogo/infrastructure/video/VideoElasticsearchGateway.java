package br.com.jkavdev.fullcycle.catalogo.infrastructure.video;

import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.domain.video.Video;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.video.VideoSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.video.persistence.VideoRepository;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
@Profile("!development")
public class VideoElasticsearchGateway implements VideoGateway {

    private static final String TITLE_PROP = "title";
    private static final String KEYWORD = ".keyword";

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
    public Optional<Video> findById(final String anId) {
        if (anId == null || anId.isBlank()) {
            return Optional.empty();
        }
        return videoRepository.findById(anId)
                .map(VideoDocument::toVideo);
    }

    @Override
    public Pagination<Video> findAll(final VideoSearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var itemsPerPage = aQuery.perPage();

        final var sort =
                Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, itemsPerPage, sort);

        final var must = new ArrayList<Query>();
        must.add(QueryBuilders.term(t -> t.field("published").value(true)));

        if (!CollectionUtils.isEmpty(aQuery.castMembers())) {
            must.add(QueryBuilders.terms(t -> t.field("cast_members").terms(it -> it.value(fieldValues(aQuery.castMembers())))));
        }
        if (!CollectionUtils.isEmpty(aQuery.categories())) {
            must.add(QueryBuilders.terms(t -> t.field("categories").terms(it -> it.value(fieldValues(aQuery.categories())))));
        }
        if (!CollectionUtils.isEmpty(aQuery.genres())) {
            must.add(QueryBuilders.terms(t -> t.field("genres").terms(it -> it.value(fieldValues(aQuery.genres())))));
        }
        if (aQuery.launchedAt() != null) {
            must.add(QueryBuilders.term(t -> t.field("launched_at").value(aQuery.launchedAt())));
        }
        if (aQuery.rating() != null && !aQuery.rating().isBlank()) {
            must.add(QueryBuilders.term(t -> t.field("rating").value(aQuery.rating())));
        }
        if (terms != null && !terms.isBlank()) {
            must.add(QueryBuilders.queryString(q -> q.fields("title", "description").query("*" + terms + "*")));
        }

        final var query = NativeQuery.builder()
                .withQuery(QueryBuilders.bool(b -> b.must(must)))
                .withPageable(page)
                .build();

        final var res = searchOperations.search(query, VideoDocument.class);
        final var total = res.getTotalHits();
        final var videos = res.stream()
                .map(SearchHit::getContent)
                .map(VideoDocument::toVideo)
                .toList();
        return new Pagination<>(currentPage, itemsPerPage, total, videos);
    }

    private List<FieldValue> fieldValues(final Set<String> ids) {
        return ids.stream()
                .map(FieldValue::of)
                .toList();
    }

    private String buildSort(final String sort) {
        if (TITLE_PROP.equals(sort)) {
            return sort.concat(KEYWORD);
        } else {
            return sort;
        }
    }

}
