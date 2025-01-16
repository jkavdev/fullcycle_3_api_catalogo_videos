package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategoryGateway;
import br.com.jkavdev.fullcycle.catalogo.domain.category.CategorySearchQuery;
import br.com.jkavdev.fullcycle.catalogo.domain.pagination.Pagination;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryDocument;
import br.com.jkavdev.fullcycle.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CategoryElasticsearchGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;
    private final SearchOperations searchOperations;

    public CategoryElasticsearchGateway(
            final CategoryRepository categoryRepository,
            final SearchOperations searchOperations
    ) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Category save(final Category aCategory) {
        categoryRepository.save(CategoryDocument.from(aCategory));
        return aCategory;
    }

    @Override
    public void deleteById(final String anId) {
        categoryRepository.deleteById(anId);
    }

    @Override
    public Optional<Category> findById(final String anId) {
        return categoryRepository.findById(anId)
                .map(CategoryDocument::toCategory);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        throw new UnsupportedOperationException();
    }
}
