package br.com.jkavdev.fullcycle.catalogo.infrastructure.category;

import br.com.jkavdev.fullcycle.catalogo.domain.category.Category;

import java.util.Optional;

public interface CategoryGateway {

    Optional<Category> categoryOfId(String anId);
}
