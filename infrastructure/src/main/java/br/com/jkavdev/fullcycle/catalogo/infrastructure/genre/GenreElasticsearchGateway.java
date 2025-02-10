package br.com.jkavdev.fullcycle.catalogo.infrastructure.genre;

import br.com.jkavdev.fullcycle.catalogo.domain.genre.Genre;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.genre.GenreSearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.genre.persistence.GenreRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreElasticsearchGateway implements GenreGateway {

    private static final String NAME_PROP = "name";
    private static final String KEYWORD = ".keyword";

    private final GenreRepository categoryRepository;

    private final SearchOperations searchOperations;

    public GenreElasticsearchGateway(
            final GenreRepository categoryRepository,
            final SearchOperations searchOperations
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Genre save(final Genre aGenre) {
        categoryRepository.save(GenreDocument.from(aGenre));
        return aGenre;
    }

    @Override
    public void deleteById(String genreId) {

    }

    @Override
    public Optional<Genre> findById(String genreId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Genre> findAll(GenreSearchQuery aQuery) {
        return null;
    }

//    @Override
//    public void deleteById(final String anId) {
//        categoryRepository.deleteById(anId);
//    }
//
//    @Override
//    public Optional<Genre> findById(final String anId) {
//        return categoryRepository.findById(anId)
//                .map(GenreDocument::toGenre);
//    }
//
//    @Override
//    public Pagination<Genre> findAll(final GenreSearchQuery aQuery) {
//        final var terms = aQuery.terms();
//        final var currentPage = aQuery.page();
//        final var perPage = aQuery.perPage();
//
//        final var sort =
//                Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
//        final var page = PageRequest.of(currentPage, perPage, sort);
//
//        final Query query;
//        if (StringUtils.isNotEmpty(terms)) {
//            final var criteia = Criteria.where("name").contains(terms)
//                    .or(Criteria.where("description").contains(terms));
//            query = new CriteriaQuery(criteia, page);
//        } else {
//            query = Query.findAll()
//                    .setPageable(page);
//        }
//
//        final var res = searchOperations.search(query, GenreDocument.class);
//        final var total = res.getTotalHits();
//        final var categories = res.stream()
//                .map(SearchHit::getContent)
//                .map(GenreDocument::toGenre)
//                .toList();
//        return new Pagination<>(currentPage, perPage, total, categories);
//    }
//
//    private String buildSort(final String sort) {
//        if (NAME_PROP.equals(sort)) {
//            return sort.concat(KEYWORD);
//        } else {
//            return sort;
//        }
//    }
}
