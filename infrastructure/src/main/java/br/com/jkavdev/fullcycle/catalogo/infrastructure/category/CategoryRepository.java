package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryRepository extends ElasticsearchRepository<CategoryDocument, String> {
}
