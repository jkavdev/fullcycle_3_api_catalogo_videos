package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryElasticsearchRepository extends ElasticsearchRepository<CategoryDocument, String> {
}
